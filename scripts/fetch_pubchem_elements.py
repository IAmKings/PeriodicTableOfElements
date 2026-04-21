#!/usr/bin/env python3
"""
PubChem Elements Data Fetcher

Fetches all 118 elements' JSON data from PubChem REST API and saves to local directory.
"""

import json
import time
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime

import requests
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry


# =============================================================================
# Configuration
# =============================================================================
BASE_URL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug_view/data"
ELEMENTS_ENDPOINT = "/element"
OUTPUT_DIR = Path("data/pubchem/elements")
RETRY_COUNT = 3
RETRY_BACKOFF_FACTOR = 2
REQUEST_DELAY = 0.3  # seconds between requests to avoid rate limiting
MAX_WORKERS = 5

# Element data: atomic number -> symbol
ELEMENTS = [
    (1, "H"), (2, "He"), (3, "Li"), (4, "Be"), (5, "B"), (6, "C"), (7, "N"), (8, "O"),
    (9, "F"), (10, "Ne"), (11, "Na"), (12, "Mg"), (13, "Al"), (14, "Si"), (15, "P"), (16, "S"),
    (17, "Cl"), (18, "Ar"), (19, "K"), (20, "Ca"), (21, "Sc"), (22, "Ti"), (23, "V"), (24, "Cr"),
    (25, "Mn"), (26, "Fe"), (27, "Co"), (28, "Ni"), (29, "Cu"), (30, "Zn"), (31, "Ga"), (32, "Ge"),
    (33, "As"), (34, "Se"), (35, "Br"), (36, "Kr"), (37, "Rb"), (38, "Sr"), (39, "Y"), (40, "Zr"),
    (41, "Nb"), (42, "Mo"), (43, "Tc"), (44, "Ru"), (45, "Rh"), (46, "Pd"), (47, "Ag"), (48, "Cd"),
    (49, "In"), (50, "Sn"), (51, "Sb"), (52, "Te"), (53, "I"), (54, "Xe"), (55, "Cs"), (56, "Ba"),
    (57, "La"), (58, "Ce"), (59, "Pr"), (60, "Nd"), (61, "Pm"), (62, "Sm"), (63, "Eu"), (64, "Gd"),
    (65, "Tb"), (66, "Dy"), (67, "Ho"), (68, "Er"), (69, "Tm"), (70, "Yb"), (71, "Lu"),
    (72, "Hf"), (73, "Ta"), (74, "W"), (75, "Re"), (76, "Os"), (77, "Ir"), (78, "Pt"), (79, "Au"),
    (80, "Hg"), (81, "Tl"), (82, "Pb"), (83, "Bi"), (84, "Po"), (85, "At"), (86, "Rn"),
    (87, "Fr"), (88, "Ra"),
    (89, "Ac"), (90, "Th"), (91, "Pa"), (92, "U"), (93, "Np"), (94, "Pu"), (95, "Am"), (96, "Cm"),
    (97, "Bk"), (98, "Cf"), (99, "Es"), (100, "Fm"), (101, "Md"), (102, "No"), (103, "Lr"),
    (104, "Rf"), (105, "Db"), (106, "Sg"), (107, "Bh"), (108, "Hs"), (109, "Mt"), (110, "Ds"), (111, "Rg"),
    (112, "Cn"), (113, "Nh"), (114, "Fl"), (115, "Mc"), (116, "Lv"), (117, "Ts"), (118, "Og")
]


# =============================================================================
# Session Setup
# =============================================================================
def create_session() -> requests.Session:
    """Create a requests session with retry strategy."""
    session = requests.Session()

    retry_strategy = Retry(
        total=RETRY_COUNT,
        backoff_factor=RETRY_BACKOFF_FACTOR,
        status_forcelist=[429, 500, 502, 503, 504],
        allowed_methods=["GET"]
    )

    adapter = HTTPAdapter(max_retries=retry_strategy, pool_connections=MAX_WORKERS, pool_maxsize=MAX_WORKERS)
    session.mount("https://", adapter)
    session.headers.update({
        "User-Agent": "PeriodicTableOfElements/1.0 (Educational Project)",
        "Accept": "application/json"
    })

    return session


# =============================================================================
# Fetch Functions
# =============================================================================
def fetch_all_elements() -> dict[int, tuple[str, dict]]:
    """
    Fetch all 118 elements from PubChem API.

    Returns:
        Dict mapping atomic number -> (symbol, json_data)
    """
    results = {}
    failed = []
    session = create_session()

    start_time = datetime.now()
    print(f"[{start_time.strftime('%H:%M:%S')}] Starting PubChem data fetch for {len(ELEMENTS)} elements")
    print(f"Output directory: {OUTPUT_DIR}")
    print("-" * 60)

    with ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        future_to_z = {
            executor.submit(fetch_single_element, session, z, symbol): z
            for z, symbol in ELEMENTS
        }

        for i, future in enumerate(as_completed(future_to_z), 1):
            z = future_to_z[future]
            symbol = next(s for az, s in ELEMENTS if az == z)

            try:
                data, error = future.result()
                if data:
                    results[z] = (symbol, data)
                    status = "OK"
                else:
                    failed.append((z, symbol, error))
                    status = f"FAIL: {error}"
            except Exception as e:
                failed.append((z, symbol, str(e)))
                status = f"ERROR: {e}"

            print(f"[{i:3d}/118] Z={z:3d} {symbol:2s} -> {status}")

            # Delay between completions to avoid overwhelming the API
            time.sleep(REQUEST_DELAY)

    elapsed = datetime.now() - start_time
    print("-" * 60)
    print(f"Completed in {elapsed}")

    return results, failed


def fetch_single_element(session: requests.Session, z: int, symbol: str) -> tuple[dict | None, str | None]:
    """
    Fetch a single element's JSON data from PubChem.

    Args:
        session: requests session
        z: atomic number
        symbol: element symbol

    Returns:
        (json_data, None) on success
        (None, error_message) on failure
    """
    url = f"{BASE_URL}{ELEMENTS_ENDPOINT}/{z}/JSON/"

    try:
        response = session.get(url, timeout=30)
        response.raise_for_status()
        data = response.json()
        return data, None
    except requests.exceptions.RequestException as e:
        return None, str(e)
    except json.JSONDecodeError as e:
        return None, f"JSON decode error: {e}"


# =============================================================================
# Save Functions
# =============================================================================
def save_elements(data: dict[int, tuple[str, dict]]) -> tuple[int, list]:
    """
    Save fetched element data to JSON files.

    Returns:
        (success_count, list of errors)
    """
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    saved = []
    errors = []

    for z, (symbol, element_data) in data.items():
        filename = f"{z:02d}-{symbol}.json"
        filepath = OUTPUT_DIR / filename

        try:
            with open(filepath, "w", encoding="utf-8") as f:
                json.dump(element_data, f, indent=2, ensure_ascii=False)
            saved.append(filename)
        except Exception as e:
            errors.append((filename, str(e)))

    return len(saved), errors


# =============================================================================
# Main
# =============================================================================
def main():
    """Main entry point."""
    print("=" * 60)
    print("  PubChem Elements Data Fetcher")
    print("=" * 60)
    print()

    # Create output directory
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    # Fetch all elements
    results, failed = fetch_all_elements()

    # Save successful results
    if results:
        saved_count, save_errors = save_elements(results)
        print(f"Saved {saved_count} element files to {OUTPUT_DIR}")
        if save_errors:
            for filename, err in save_errors:
                print(f"  ERROR saving {filename}: {err}")

    # Report failures
    if failed:
        print(f"\nFailed to fetch {len(failed)} elements:")
        for z, symbol, error in failed:
            print(f"  Z={z:3d} {symbol:2s}: {error}")
    else:
        print("\nAll 118 elements fetched successfully!")

    print()
    print("=" * 60)
    print("Done!")
    print("=" * 60)


if __name__ == "__main__":
    main()