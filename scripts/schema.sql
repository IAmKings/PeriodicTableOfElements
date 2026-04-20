-- Periodic Table of Elements - SQLite Schema
-- Based on periodic_table_of_elements_sqlite_design.md

-- Elements Master Table: Basic element information
CREATE TABLE IF NOT EXISTS Elements_Master (
    Symbol                  VARCHAR(3) PRIMARY KEY,
    Z                       INTEGER NOT NULL UNIQUE,
    Name_CN                 VARCHAR(10) NOT NULL,
    Name_EN                 VARCHAR(20) NOT NULL,
    Ar                      REAL,
    MassNumber_MostStable   INTEGER,
    Period                  INTEGER NOT NULL,
    "Group"                 INTEGER,
    Group_Traditional       VARCHAR(5),
    Block                   CHAR(1) CHECK(Block IN ('s', 'p', 'd', 'f')),
    Category                VARCHAR(20),
    Subcategory             VARCHAR(30),
    ElectronConfig_Full     VARCHAR(50),
    ElectronConfig_Simplified VARCHAR(50),
    ValenceElectrons        VARCHAR(20),
    UnpairedElectrons       INTEGER,
    Phase_STP               VARCHAR(10) CHECK(Phase_STP IN ('Solid', 'Liquid', 'Gas', 'Unknown')),
    CrystalStructure        VARCHAR(20),
    MeltingPoint_K          REAL,
    BoilingPoint_K          REAL,
    Density_gcm3            REAL,
    ExamFrequency           VARCHAR(10) CHECK(ExamFrequency IN ('High', 'Medium', 'Low')),
    KeyExamPoints           TEXT,
    Data_Source             VARCHAR(50) DEFAULT 'Wikipedia/PubChem',
    Data_Status             VARCHAR(20) DEFAULT 'Active' CHECK(Data_Status IN ('Active', 'Deprecated', 'Superseded')),
    Date_Collected          DATE DEFAULT (DATE('now')),
    Date_Verified           DATE,
    Data_Version            VARCHAR(10) DEFAULT '1.0.0',
    Notes                   TEXT
);

-- Periodic Properties Table: Periodic trends data
CREATE TABLE IF NOT EXISTS Properties_Periodic (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    Symbol                  VARCHAR(3) NOT NULL,
    Z                       INTEGER NOT NULL,
    CovalentRadius_pm       REAL,
    MetallicRadius_pm       REAL,
    VanderWaalsRadius_pm    REAL,
    IE1_kJmol               REAL,
    IE2_kJmol               REAL,
    IE3_kJmol               REAL,
    IE4_kJmol               REAL,
    EA_kJmol                REAL,
    Electronegativity_Pauling REAL,
    Electronegativity_Mulliken REAL,
    Electronegativity_AllredRochow REAL,
    E0_Acidic_V             REAL,
    E0_Basic_V              REAL,
    LatimerDiagram          TEXT,
    OxidationStates_Common  TEXT,
    Data_Source             VARCHAR(50) DEFAULT 'Wikipedia/PubChem',
    Data_Status             VARCHAR(20) DEFAULT 'Active',
    Date_Collected          DATE DEFAULT (DATE('now')),
    Notes                   TEXT,
    FOREIGN KEY (Symbol) REFERENCES Elements_Master(Symbol),
    FOREIGN KEY (Z) REFERENCES Elements_Master(Z),
    UNIQUE(Symbol, Z)
);

-- Indexes for common queries
CREATE INDEX IF NOT EXISTS idx_elements_period ON Elements_Master(Period);
CREATE INDEX IF NOT EXISTS idx_elements_group ON Elements_Master("Group");
CREATE INDEX IF NOT EXISTS idx_elements_block ON Elements_Master(Block);
CREATE INDEX IF NOT EXISTS idx_elements_category ON Elements_Master(Category);
CREATE INDEX IF NOT EXISTS idx_properties_symbol ON Properties_Periodic(Symbol);
