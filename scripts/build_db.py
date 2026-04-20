#!/usr/bin/env python3
"""
Periodic Table Database Builder
Scrapes Wikipedia (Chinese + English) and PubChem API to build SQLite database.
Usage: python scripts/build_db.py
"""

import sqlite3
import json
import time
import logging
import re
import sys
from pathlib import Path
from typing import Optional, Dict, Any, List, Tuple
from dataclasses import dataclass, field
from datetime import date

import requests
from bs4 import BeautifulSoup

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
PROJECT_ROOT = Path(__file__).parent.parent
DB_PATH = PROJECT_ROOT / "periodic_table.db"
ELEMENT_LIST_PATH = PROJECT_ROOT / "scripts" / "element_list.json"
SCHEMA_PATH = PROJECT_ROOT / "scripts" / "schema.sql"
ERROR_LOG_PATH = PROJECT_ROOT / "scripts" / "scrape_errors.log"
REQUEST_DELAY = 1.2  # seconds between requests — be polite

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
    handlers=[logging.StreamHandler(sys.stdout)],
)
logger = logging.getLogger("build_db")


# ---------------------------------------------------------------------------
# Data classes
# ---------------------------------------------------------------------------
@dataclass
class ElementData:
    """Unified data container for one element."""

    Symbol: str
    Z: int
    Name_CN: str
    Name_EN: str

    # Periodic position
    Period: Optional[int] = None
    Group: Optional[int] = None
    Group_Traditional: Optional[str] = None
    Block: Optional[str] = None
    Category: Optional[str] = None
    Subcategory: Optional[str] = None

    # Atomic properties
    Ar: Optional[float] = None
    MassNumber_MostStable: Optional[int] = None
    ElectronConfig_Full: Optional[str] = None
    ElectronConfig_Simplified: Optional[str] = None
    ValenceElectrons: Optional[str] = None
    UnpairedElectrons: Optional[int] = None

    # Physical properties
    Phase_STP: Optional[str] = None
    CrystalStructure: Optional[str] = None
    MeltingPoint_K: Optional[float] = None
    BoilingPoint_K: Optional[float] = None
    Density_gcm3: Optional[float] = None

    # Exam metadata
    ExamFrequency: Optional[str] = None
    KeyExamPoints: Optional[str] = None

    # Provenance
    Data_Source: str = "Wikipedia"
    Data_Status: str = "Active"
    Date_Collected: str = field(default_factory=lambda: date.today().isoformat())
    Data_Version: str = "1.0.0"
    Notes: Optional[str] = None


@dataclass
class PropertiesData:
    """Periodic properties for one element."""

    Symbol: str
    Z: int

    CovalentRadius_pm: Optional[float] = None
    MetallicRadius_pm: Optional[float] = None
    VanderWaalsRadius_pm: Optional[float] = None

    IE1_kJmol: Optional[float] = None
    IE2_kJmol: Optional[float] = None
    IE3_kJmol: Optional[float] = None
    IE4_kJmol: Optional[float] = None

    EA_kJmol: Optional[float] = None
    Electronegativity_Pauling: Optional[float] = None
    Electronegativity_Mulliken: Optional[float] = None
    Electronegativity_AllredRochow: Optional[float] = None

    E0_Acidic_V: Optional[float] = None
    E0_Basic_V: Optional[float] = None
    OxidationStates_Common: Optional[str] = None

    Data_Source: str = "Wikipedia/PubChem"
    Date_Collected: str = field(default_factory=lambda: date.today().isoformat())
    Notes: Optional[str] = None


# ---------------------------------------------------------------------------
# Wikipedia scraper
# ---------------------------------------------------------------------------
class WikipediaScraper:
    """Fetch and parse Wikipedia element pages."""

    def __init__(self, delay: float = REQUEST_DELAY):
        self.delay = delay
        self.session = requests.Session()
        self.session.headers.update(
            {
                "User-Agent": (
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
                    "AppleWebKit/537.36 (KHTML, like Gecko) "
                    "Chrome/120.0.0.0 Safari/537.36"
                ),
                "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8",
            }
        )
        self.errors: List[Dict[str, Any]] = []

    # ---- generic helpers ---------------------------------------------------

    def _get(self, url: str) -> Optional[BeautifulSoup]:
        """GET page and return BeautifulSoup; log errors."""
        try:
            resp = self.session.get(url, timeout=30)
            resp.raise_for_status()
            time.sleep(self.delay)
            return BeautifulSoup(resp.text, "lxml")
        except requests.RequestException as exc:
            logger.error("Failed to fetch %s: %s", url, exc)
            self.errors.append({"url": url, "error": str(exc)})
            return None

    @staticmethod
    def _clean_text(text: Optional[str]) -> Optional[str]:
        if not text:
            return None
        # strip citations like [1], [2], [note 1]
        text = re.sub(r"\[\d+\]", "", text)
        text = re.sub(r"\[note\s*\d+\]", "", text)
        text = re.sub(r"\[.*?\]", "", text)
        # strip trailing Chinese noise like "2, 6 氧的电子层（2, 6）"
        text = re.sub(r"[\d,\s]+[\u4e00-\u9fff].*$", "", text)
        return text.strip() or None

    @staticmethod
    def _find_infobox_row(soup: BeautifulSoup, *keywords: str) -> Optional[str]:
        """Search infobox rows by header text (case-insensitive).
        Handles both normal <th>+<td> rows and colspan="2" header rows
        where data is on the next line with empty <th>."""
        infobox = soup.find("table", class_="infobox")
        if not infobox:
            return None
        rows = list(infobox.find_all("tr"))
        for i, tr in enumerate(rows):
            th = tr.find("th")
            if not th:
                continue
            header_text = th.get_text(strip=True).lower()
            if any(kw.lower() in header_text for kw in keywords):
                td = tr.find("td")
                if td:
                    return td.get_text(separator=" ", strip=True)
                # Handle colspan="2" header: data is on next row with empty <th>
                if i + 1 < len(rows):
                    next_tr = rows[i + 1]
                    next_td = next_tr.find("td")
                    if next_td:
                        return next_td.get_text(separator=" ", strip=True)
        return None

    # ---- temperature / density parsing -------------------------------------

    @staticmethod
    def _parse_temperature(text: Optional[str]) -> Optional[float]:
        """Extract Kelvin value from temperature text."""
        if not text:
            return None
        # Patterns:
        # "1538 °C (2800 °F, 1811 K)"
        # "−259.16 °C, 14.01 K (−434.49 °F)"
        # "14.01 K (−259.14 °C, −434.45 °F)"
        kelvin_match = re.search(r"([−\-]?\d+\.?\d*)\s*K", text)
        if kelvin_match:
            val = kelvin_match.group(1).replace("−", "-")
            try:
                return float(val)
            except ValueError:
                pass
        # Try Celsius conversion
        celsius_match = re.search(r"([−\-]?\d+\.?\d*)\s*°C", text)
        if celsius_match:
            val = celsius_match.group(1).replace("−", "-")
            try:
                return float(val) + 273.15
            except ValueError:
                pass
        return None

    @staticmethod
    def _parse_density(text: Optional[str]) -> Optional[float]:
        """Extract density in g/cm³."""
        if not text:
            return None
        # "7.874 g/cm³" or "7.874 g·cm−3" or "0.08988 g/L" (gas)
        # Also handle "~1.5 (predicted)"
        m = re.search(r"([~≈]?\s*[\d\.]+)\s*(?:g/cm³|g·cm|g\.cm|g\s*cm)", text)
        if m:
            val = m.group(1).replace("~", "").replace("≈", "").strip()
            try:
                return float(val)
            except ValueError:
                pass
        # Gas density at STP — convert g/L to g/cm³ (×0.001)
        m = re.search(r"([\d\.]+)\s*g/L", text)
        if m:
            try:
                return float(m.group(1)) * 0.001
            except ValueError:
                pass
        return None

    @staticmethod
    def _parse_number(text: Optional[str]) -> Optional[float]:
        """Extract first float from text."""
        if not text:
            return None
        # Remove uncertainty notation like "(2)" or parentheses
        text = re.sub(r"\(\d+\)", "", text)
        text = re.sub(r"\[.*?\]", "", text)
        m = re.search(r"[−\-]?\d+\.?\d*", text)
        if m:
            val = m.group().replace("−", "-")
            try:
                return float(val)
            except ValueError:
                pass
        return None

    @staticmethod
    def _parse_int(text: Optional[str]) -> Optional[int]:
        """Extract first int from text."""
        if not text:
            return None
        text = re.sub(r"\(\d+\)", "", text)
        text = re.sub(r"\[.*?\]", "", text)
        m = re.search(r"[−\-]?\d+", text)
        if m:
            val = m.group().replace("−", "-")
            try:
                return int(val)
            except ValueError:
                pass
        return None

    # ---- phase / category helpers -----------------------------------------

    @staticmethod
    def _normalize_phase(text: Optional[str]) -> Optional[str]:
        if not text:
            return None
        text = text.lower()
        if "solid" in text or "固" in text:
            return "Solid"
        if "liquid" in text or "液" in text:
            return "Liquid"
        if "gas" in text or "气" in text:
            return "Gas"
        return "Unknown"

    @staticmethod
    def _infer_period(z: int) -> int:
        if z <= 2:
            return 1
        if z <= 10:
            return 2
        if z <= 18:
            return 3
        if z <= 36:
            return 4
        if z <= 54:
            return 5
        if z <= 86:
            return 6
        return 7

    # ---- Chinese Wikipedia -------------------------------------------------

    def fetch_zh(self, name_cn: str, symbol: str, z: int) -> Tuple[ElementData, PropertiesData]:
        """Fetch and parse Chinese Wikipedia page."""
        url = f"https://zh.wikipedia.org/wiki/{name_cn}"
        logger.info("[ZH] Fetching %s (%s)...", symbol, name_cn)
        soup = self._get(url)

        elem = ElementData(Symbol=symbol, Z=z, Name_CN=name_cn, Name_EN="")
        props = PropertiesData(Symbol=symbol, Z=z)

        if not soup:
            elem.Notes = f"Failed to fetch Chinese Wikipedia: {url}"
            return elem, props

        # Parse infobox
        elem.Period = self._infer_period(z)

        # Group (原子序数对应的族)
        group_text = self._find_infobox_row(soup, "族", "group")
        if group_text:
            elem.Group = self._parse_int(group_text)

        # Block
        block_text = self._find_infobox_row(soup, "区", "block")
        if block_text:
            m = re.search(r"([spdf])", block_text.lower())
            if m:
                elem.Block = m.group(1)

        # Category / 分类
        cat_text = self._find_infobox_row(soup, "分类", "category")
        if cat_text:
            elem.Category = self._clean_text(cat_text)

        # Atomic mass
        mass_text = self._find_infobox_row(soup, "原子量", "atomic mass", "相对原子质量")
        if mass_text:
            elem.Ar = self._parse_number(mass_text)
            # Chinese Wikipedia lists molecular weight for diatomic elements
            diatomic = {"H", "N", "O", "F", "Cl", "Br", "I"}
            if elem.Symbol in diatomic and elem.Ar:
                typical = {"H": 1.0, "N": 14.0, "O": 16.0, "F": 19.0, "Cl": 35.5, "Br": 80.0, "I": 127.0}
                if elem.Ar > typical.get(elem.Symbol, 0) * 1.5:
                    elem.Ar = round(elem.Ar / 2, 6)

        # Electron configuration
        ec_text = self._find_infobox_row(soup, "电子排布", "electron configuration")
        if ec_text:
            ec_clean = self._clean_text(ec_text)
            elem.ElectronConfig_Simplified = ec_clean
            elem.ElectronConfig_Full = ec_clean

        # Phase
        phase_text = self._find_infobox_row(soup, "物态", "phase", "物质状态")
        if phase_text:
            elem.Phase_STP = self._normalize_phase(phase_text)

        # Melting point
        mp_text = self._find_infobox_row(soup, "熔点", "melting point")
        if mp_text:
            elem.MeltingPoint_K = self._parse_temperature(mp_text)

        # Boiling point
        bp_text = self._find_infobox_row(soup, "沸点", "boiling point")
        if bp_text:
            elem.BoilingPoint_K = self._parse_temperature(bp_text)

        # Density
        dens_text = self._find_infobox_row(soup, "密度", "density")
        if dens_text:
            elem.Density_gcm3 = self._parse_density(dens_text)

        # Crystal structure
        cs_text = self._find_infobox_row(soup, "晶体结构", "crystal structure")
        if cs_text:
            elem.CrystalStructure = self._clean_text(cs_text)

        # Electronegativity
        en_text = self._find_infobox_row(soup, "电负性", "electronegativity")
        if en_text:
            props.Electronegativity_Pauling = self._parse_number(en_text)

        # Oxidation states
        ox_text = self._find_infobox_row(soup, "氧化态", "oxidation state")
        if ox_text:
            props.OxidationStates_Common = self._clean_text(ox_text)

        return elem, props

    # ---- English Wikipedia -------------------------------------------------

    def fetch_en(
        self, name_en: str, base_elem: ElementData, base_props: PropertiesData
    ) -> Tuple[ElementData, PropertiesData]:
        """Fetch and parse English Wikipedia page; merge with existing data."""
        url = f"https://en.wikipedia.org/wiki/{name_en}"
        logger.info("[EN] Fetching %s (%s)...", base_elem.Symbol, name_en)
        soup = self._get(url)

        if not soup:
            base_elem.Notes = (base_elem.Notes or "") + f" | Failed EN wiki: {url}"
            return base_elem, base_props

        # Always use English Wikipedia page title as the canonical English name
        title = soup.find("h1", id="firstHeading")
        if title:
            base_elem.Name_EN = title.get_text(strip=True)

        # Group
        if base_elem.Group is None:
            group_text = self._find_infobox_row(soup, "group")
            if group_text:
                base_elem.Group = self._parse_int(group_text)

        # Period
        if base_elem.Period is None:
            period_text = self._find_infobox_row(soup, "period")
            if period_text:
                base_elem.Period = self._parse_int(period_text)

        # Block
        if base_elem.Block is None:
            block_text = self._find_infobox_row(soup, "block")
            if block_text:
                m = re.search(r"([spdf])", block_text.lower())
                if m:
                    base_elem.Block = m.group(1)

        # Category
        if base_elem.Category is None:
            cat_text = self._find_infobox_row(soup, "element category")
            if cat_text:
                base_elem.Category = self._clean_text(cat_text)

        # Atomic mass — always prefer English Wikipedia's "standard atomic weight"
        # because Chinese Wikipedia may list molecular weight for diatomic elements.
        mass_text = self._find_infobox_row(soup, "standard atomic weight")
        if mass_text:
            base_elem.Ar = self._parse_number(mass_text)
        else:
            # fallback to "atomic weight" if standard not present
            mass_text = self._find_infobox_row(soup, "atomic weight")
            if mass_text:
                base_elem.Ar = self._parse_number(mass_text)

        # Fix diatomic molecular weights that slipped through
        diatomic = {"H", "N", "O", "F", "Cl", "Br", "I"}
        if base_elem.Symbol in diatomic and base_elem.Ar:
            # Typical atomic weights: H~1, N~14, O~16, F~19, Cl~35.5, Br~80, I~127
            # If captured value is roughly 2x, it's the molecular weight
            typical = {"H": 1.0, "N": 14.0, "O": 16.0, "F": 19.0, "Cl": 35.5, "Br": 80.0, "I": 127.0}
            if base_elem.Ar > typical.get(base_elem.Symbol, 0) * 1.5:
                base_elem.Ar = round(base_elem.Ar / 2, 6)

        # Electron configuration
        if base_elem.ElectronConfig_Simplified is None:
            ec_text = self._find_infobox_row(soup, "electron configuration")
            if ec_text:
                ec_clean = self._clean_text(ec_text)
                base_elem.ElectronConfig_Simplified = ec_clean
                base_elem.ElectronConfig_Full = ec_clean

        # Phase
        if base_elem.Phase_STP is None:
            phase_text = self._find_infobox_row(soup, "phase")
            if phase_text:
                base_elem.Phase_STP = self._normalize_phase(phase_text)

        # Melting point
        if base_elem.MeltingPoint_K is None:
            mp_text = self._find_infobox_row(soup, "melting point")
            if mp_text:
                base_elem.MeltingPoint_K = self._parse_temperature(mp_text)

        # Boiling point
        if base_elem.BoilingPoint_K is None:
            bp_text = self._find_infobox_row(soup, "boiling point")
            if bp_text:
                base_elem.BoilingPoint_K = self._parse_temperature(bp_text)

        # Density
        if base_elem.Density_gcm3 is None:
            dens_text = self._find_infobox_row(soup, "density")
            if dens_text:
                base_elem.Density_gcm3 = self._parse_density(dens_text)

        # Crystal structure
        if base_elem.CrystalStructure is None:
            cs_text = self._find_infobox_row(soup, "crystal structure")
            if cs_text:
                base_elem.CrystalStructure = self._clean_text(cs_text)

        # ---- Properties_Periodic ------------------------------------------

        # Covalent radius — look for number before "pm" to avoid catching
        # hybridisation prefixes like "sp3: 77 pm" -> 77 (not 3)
        if base_props.CovalentRadius_pm is None:
            r_text = self._find_infobox_row(soup, "covalent radius")
            if r_text:
                m = re.search(r"(\d{2,3})\s*pm", r_text)
                if m:
                    base_props.CovalentRadius_pm = float(m.group(1))
                else:
                    base_props.CovalentRadius_pm = self._parse_number(r_text)

        # Van der Waals radius
        if base_props.VanderWaalsRadius_pm is None:
            vdw_text = self._find_infobox_row(soup, "van der waals radius")
            if vdw_text:
                m = re.search(r"(\d{2,3})\s*pm", vdw_text)
                if m:
                    base_props.VanderWaalsRadius_pm = float(m.group(1))
                else:
                    base_props.VanderWaalsRadius_pm = self._parse_number(vdw_text)

        # Ionization energies
        ie_text = self._find_infobox_row(soup, "ionization energies", "ionisation energies")
        if ie_text:
            # Format: "1st: 1312.0 kJ/mol  2nd: ..."
            ie_vals = re.findall(r"(\d+(?:\.\d+)?)\s*kJ/mol", ie_text)
            if len(ie_vals) >= 1:
                base_props.IE1_kJmol = float(ie_vals[0])
            if len(ie_vals) >= 2:
                base_props.IE2_kJmol = float(ie_vals[1])
            if len(ie_vals) >= 3:
                base_props.IE3_kJmol = float(ie_vals[2])
            if len(ie_vals) >= 4:
                base_props.IE4_kJmol = float(ie_vals[3])

        # Electronegativity
        if base_props.Electronegativity_Pauling is None:
            en_text = self._find_infobox_row(soup, "electronegativity")
            if en_text:
                # Look for Pauling value specifically
                pauling_match = re.search(r"Pauling.*?([\d\.]+)", en_text, re.I)
                if pauling_match:
                    base_props.Electronegativity_Pauling = float(pauling_match.group(1))
                else:
                    base_props.Electronegativity_Pauling = self._parse_number(en_text)

        # Electron affinity
        if base_props.EA_kJmol is None:
            ea_text = self._find_infobox_row(soup, "electron affinity")
            if ea_text:
                base_props.EA_kJmol = self._parse_number(ea_text)

        # Oxidation states
        if base_props.OxidationStates_Common is None:
            ox_text = self._find_infobox_row(soup, "oxidation states")
            if ox_text:
                base_props.OxidationStates_Common = self._clean_text(ox_text)

        return base_elem, base_props


# ---------------------------------------------------------------------------
# PubChem client
# ---------------------------------------------------------------------------
class PubChemClient:
    """Lightweight PubChem PUG REST client for element data."""

    def __init__(self, delay: float = REQUEST_DELAY):
        self.delay = delay
        self.session = requests.Session()
        self.session.headers.update(
            {"User-Agent": "PeriodicTableBot/1.0 (Educational Research)"}
        )

    def get_properties(self, symbol: str) -> Dict[str, Any]:
        """Fetch numeric properties for element by symbol."""
        result: Dict[str, Any] = {}
        # PubChem compound lookup by name often returns the element's
        # most common compound; we treat it as best-effort supplementary data.
        url = (
            "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/"
            f"{symbol}/property/MolecularWeight,IsotopeAtomCount/JSON"
        )
        try:
            resp = self.session.get(url, timeout=20)
            if resp.status_code == 200:
                data = resp.json()
                props = data.get("PropertyTable", {}).get("Properties", [{}])[0]
                mw = props.get("MolecularWeight")
                if mw:
                    result["molecular_weight"] = float(mw)
            time.sleep(self.delay)
        except (requests.RequestException, ValueError) as exc:
            logger.warning("PubChem lookup failed for %s: %s", symbol, exc)
        return result


# ---------------------------------------------------------------------------
# Database layer
# ---------------------------------------------------------------------------
class Database:
    """SQLite database operations for the periodic table."""

    def __init__(self, db_path: Path):
        self.conn = sqlite3.connect(db_path)
        self.conn.row_factory = sqlite3.Row

    def init_schema(self, schema_path: Path) -> None:
        """Load and execute the SQL schema from the schema file."""
        with open(schema_path, encoding="utf-8") as f:
            self.conn.executescript(f.read())
        self.conn.commit()
        logger.info("Database schema initialized.")

    def insert_element(self, data: ElementData) -> None:
        """Insert an ElementData record into Elements_Master."""
        fields = [
            "Symbol", "Z", "Name_CN", "Name_EN", "Ar", "MassNumber_MostStable",
            "Period", "Group", "Group_Traditional", "Block", "Category", "Subcategory",
            "ElectronConfig_Full", "ElectronConfig_Simplified", "ValenceElectrons",
            "UnpairedElectrons", "Phase_STP", "CrystalStructure", "MeltingPoint_K",
            "BoilingPoint_K", "Density_gcm3", "ExamFrequency", "KeyExamPoints",
            "Data_Source", "Data_Status", "Date_Collected", "Data_Version", "Notes",
        ]
        placeholders = ", ".join("?" * len(fields))
        quoted_fields = ', '.join(f'"{f}"' for f in fields)
        sql = f"INSERT INTO Elements_Master ({quoted_fields}) VALUES ({placeholders})"
        values = [getattr(data, f) for f in fields]
        self.conn.execute(sql, values)

    def insert_properties(self, data: PropertiesData) -> None:
        """Insert a PropertiesData record into Properties_Periodic."""
        fields = [
            "Symbol", "Z", "CovalentRadius_pm", "MetallicRadius_pm",
            "VanderWaalsRadius_pm", "IE1_kJmol", "IE2_kJmol", "IE3_kJmol",
            "IE4_kJmol", "EA_kJmol", "Electronegativity_Pauling",
            "Electronegativity_Mulliken", "Electronegativity_AllredRochow",
            "E0_Acidic_V", "E0_Basic_V", "OxidationStates_Common",
            "Data_Source", "Date_Collected", "Notes",
        ]
        placeholders = ", ".join("?" * len(fields))
        quoted_fields = ', '.join(f'"{f}"' for f in fields)
        sql = f"INSERT INTO Properties_Periodic ({quoted_fields}) VALUES ({placeholders})"
        values = [getattr(data, f) for f in fields]
        self.conn.execute(sql, values)

    def element_exists(self, symbol: str) -> bool:
        """Check whether an element record already exists in the database."""
        cur = self.conn.execute(
            "SELECT 1 FROM Elements_Master WHERE Symbol = ?", (symbol,)
        )
        return cur.fetchone() is not None

    def commit(self) -> None:
        """Commit the current transaction."""
        self.conn.commit()

    def close(self) -> None:
        """Close the database connection."""
        self.conn.close()


# ---------------------------------------------------------------------------
# Post-processing helpers
# ---------------------------------------------------------------------------

def derive_subcategory(elem: ElementData) -> None:
    """Derive Subcategory from Category + Block + Group."""
    if elem.Subcategory:
        return
    cat = (elem.Category or "").lower()
    block = elem.Block
    group = elem.Group

    # H is special: s-block but NOT an alkali metal — it's a reactive nonmetal
    if elem.Symbol == "H":
        elem.Subcategory = "Reactive Nonmetal"
    elif "alkali" in cat or (block == "s" and group == 1 and elem.Z > 1):
        elem.Subcategory = "Alkali Metal"
    elif "alkaline" in cat or (block == "s" and group == 2):
        elem.Subcategory = "Alkaline Earth Metal"
    elif "transition" in cat or (block == "d"):
        elem.Subcategory = "Transition Metal"
    elif "lanthanide" in cat or (block == "f" and elem.Z <= 71):
        elem.Subcategory = "Lanthanide"
    elif "actinide" in cat or (block == "f" and elem.Z > 71):
        elem.Subcategory = "Actinide"
    elif "metalloid" in cat:
        elem.Subcategory = "Metalloid"
    elif "noble gas" in cat or (block == "p" and group == 18):
        elem.Subcategory = "Noble Gas"
    elif "nonmetal" in cat:
        if elem.Symbol == "C":
            elem.Subcategory = "Polyatomic Nonmetal"
        else:
            elem.Subcategory = "Reactive Nonmetal"
    elif block == "p":
        # p-block: classify by group — includes halogens, reactive nonmetals, post-transition
        if group == 17:
            elem.Subcategory = "Halogen"
        elif group in (15, 16):
            elem.Subcategory = "Reactive Nonmetal"
        elif group in (13, 14):
            elem.Subcategory = "Post-transition Metal"
        else:
            elem.Subcategory = "Other Metal"
    elif group == 18:
        elem.Subcategory = "Noble Gas"
    else:
        elem.Subcategory = "Other Metal"


def derive_group_traditional(elem: ElementData) -> None:
    """Map IUPAC Group (1-18) to traditional A/B notation (ACS convention)."""
    if elem.Group_Traditional or not elem.Group:
        return
    g = elem.Group
    mapping = {
        1: "IA", 2: "IIA",
        3: "IIIB", 4: "IVB", 5: "VB", 6: "VIB", 7: "VIIB",
        8: "VIII", 9: "VIII", 10: "VIII",
        11: "IB", 12: "IIB",
        13: "IIIA", 14: "IVA", 15: "VA", 16: "VIA", 17: "VIIA", 18: "VIIIA",
    }
    elem.Group_Traditional = mapping.get(g)


def derive_valence_electrons(elem: ElementData) -> None:
    """Derive valence electrons from simplified electron configuration."""
    if elem.ValenceElectrons or not elem.ElectronConfig_Simplified:
        return
    ec = elem.ElectronConfig_Simplified
    # Extract last orbital(s): e.g., "[Ar] 3d⁶ 4s²" -> "3d⁶ 4s²"
    # or "[He] 2s² 2p⁴" -> "2s² 2p⁴"
    m = re.search(r"\[.*?\]\s*(.+)", ec)
    if m:
        elem.ValenceElectrons = m.group(1).strip()
    else:
        elem.ValenceElectrons = ec.strip()


def derive_unpaired_electrons(elem: ElementData) -> None:
    """Estimate unpaired electrons from valence electron configuration."""
    if elem.UnpairedElectrons is not None or not elem.ValenceElectrons:
        return
    # Count unpaired: e.g., "3d⁵ 4s¹" -> 5+1=6; "2p⁴" -> 2
    total_unpaired = 0
    orbitals = re.findall(r"(\d)([spdf])\^?(\d+)", elem.ValenceElectrons)
    for _n, l, count in orbitals:
        count = int(count)
        max_e = {"s": 2, "p": 6, "d": 10, "f": 14}.get(l, 2)
        if count <= max_e // 2:
            total_unpaired += count
        else:
            total_unpaired += max_e - count
    elem.UnpairedElectrons = total_unpaired if total_unpaired > 0 else None


# ---------------------------------------------------------------------------
# Exam frequency defaults (high-value elements for exams)
# ---------------------------------------------------------------------------

# Element symbols that appear frequently in exam questions
_EXAM_HIGH = {
    "H", "He", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar",
    "K", "Ca", "Fe", "Cu", "Zn", "Br", "Ag", "I", "Au", "Pb",
}
_EXAM_MEDIUM = {
    "Li", "Be", "B", "Ge", "As", "Se", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc",
    "Ru", "Rh", "Pd", "Cd", "Sn", "Sb", "Te", "Xe", "Cs", "Ba", "La", "Ce", "Gd",
    "Hg", "Tl", "Bi", "Po", "Rn", "Fr", "Ra", "Th", "U", "Mn", "Co", "Ni",
}


def set_exam_defaults(elem: ElementData) -> None:
    """Set ExamFrequency and KeyExamPoints defaults if not already set."""
    if elem.ExamFrequency is None:
        if elem.Symbol in _EXAM_HIGH:
            elem.ExamFrequency = "High"
        elif elem.Symbol in _EXAM_MEDIUM:
            elem.ExamFrequency = "Medium"
        else:
            elem.ExamFrequency = "Low"

    if elem.KeyExamPoints is None:
        # Build minimal placeholder from available data
        pts = []
        if elem.Subcategory:
            pts.append(elem.Subcategory)
        if elem.ElectronConfig_Simplified:
            pts.append(f"电子构型:{elem.ElectronConfig_Simplified}")
        elem.KeyExamPoints = "; ".join(pts) if pts else "周期表基础"


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main() -> None:
    """Entry point: build the periodic table database from scratch."""
    # Idempotent: don't delete existing DB — re-runs merge/upsert cleanly
    if DB_PATH.exists():
        logger.info("Using existing database at %s (will skip existing elements).", DB_PATH)

    # Load element list
    with open(ELEMENT_LIST_PATH, encoding="utf-8") as f:
        element_list: List[Dict[str, Any]] = json.load(f)

    # Init database
    db = Database(DB_PATH)
    db.init_schema(SCHEMA_PATH)

    # Init scrapers
    wiki = WikipediaScraper()
    pubchem = PubChemClient()

    total = len(element_list)
    success = 0

    for idx, elem_info in enumerate(element_list, 1):
        symbol = elem_info["Symbol"]
        z = elem_info["Z"]
        name_cn = elem_info["Name_CN"]
        name_en = elem_info["Name_EN"]

        logger.info("=" * 50)
        logger.info("[%d/%d] Processing %s (Z=%d)...", idx, total, symbol, z)

        # Skip if already inserted
        if db.element_exists(symbol):
            logger.info("  Already exists, skipping.")
            continue

        # 1) Chinese Wikipedia
        elem, props = wiki.fetch_zh(name_cn, symbol, z)

        # 2) English Wikipedia (merge)
        elem, props = wiki.fetch_en(name_en, elem, props)

        # 3) PubChem (supplementary)
        pc_data = pubchem.get_properties(symbol)
        if pc_data.get("molecular_weight") and elem.Ar is None:
            elem.Ar = pc_data["molecular_weight"]

        # 4) Post-process derived fields
        derive_subcategory(elem)
        derive_group_traditional(elem)
        derive_valence_electrons(elem)
        derive_unpaired_electrons(elem)
        set_exam_defaults(elem)

        # 5) Insert into DB
        try:
            db.insert_element(elem)
            db.insert_properties(props)
            db.commit()
            success += 1
            logger.info("  ✓ Inserted %s", symbol)
        except sqlite3.Error as exc:
            logger.error("  ✗ Failed to insert %s: %s", symbol, exc)
            wiki.errors.append({"symbol": symbol, "phase": "insert", "error": str(exc)})

    db.close()

    # Write error log
    if wiki.errors:
        with open(ERROR_LOG_PATH, "w", encoding="utf-8") as f:
            json.dump(wiki.errors, f, ensure_ascii=False, indent=2)
        logger.warning("Errors logged to %s (%d errors)", ERROR_LOG_PATH, len(wiki.errors))
    else:
        logger.info("No errors encountered.")

    logger.info("=" * 50)
    logger.info("Done! %d/%d elements inserted.", success, total)
    logger.info("Database: %s", DB_PATH.resolve())


if __name__ == "__main__":
    main()
