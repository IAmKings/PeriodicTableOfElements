# 元素周期表 SQLite 数据库

基于维基百科（中文/英文）和 PubChem PUG REST API 抓取的 118 种化学元素数据，生成 SQLite 关系型数据库，并实现 PubChem 交叉验证质量评分系统。

---

## 项目结构

```
.
├── periodic_table.db              # 生成的 SQLite 数据库（3 表，118 元素）
├── periodic_table_of_elements_sqlite_design.md  # 数据库设计文档
├── scripts/
│   ├── build_db.py                # 主抓取脚本（幂等运行）
│   ├── schema.sql                 # 数据库表结构定义
│   ├── element_list.json          # 118 元素基础清单（Z, Symbol, Name_CN, Name_EN）
│   └── requirements.txt           # Python 依赖
└── README.md                      # 本文件
```

---

## 环境要求

- Python 3.11+
- 网络连接（抓取维基百科和 PubChem）

---

## 安装依赖

```bash
cd scripts
pip install -r requirements.txt
```

依赖包：
- `requests` — HTTP 请求
- `beautifulsoup4` — HTML 解析
- `lxml` — BeautifulSoup 解析后端

---

## 运行脚本

### 生成/重建数据库

```bash
python3 scripts/build_db.py
```

脚本行为（幂等设计）：
1. 初始化数据库 schema（如不存在则创建表）
2. 按原子序数 Z=1~118 逐个处理
3. **先抓取 PubChem**（独立于 DB 状态），存储原始快照
4. **如元素已存在于 DB 则跳过**（幂等保障）
5. 抓取**中文维基百科**获取中文名和基础属性
6. 抓取**英文维基百科**补充详细数据
7. 将 PubChem 数据与 Wikipedia 数据对比，计算 **Quality_Score (0~3)**
8. 从 PubChem 补充 Wikipedia 缺失字段（EA、EN_Mulliken）
9. 写入数据库，抓取间隔约 1.2 秒

总耗时约 **10~12 分钟**（118 元素 × 2~3 次 Wikipedia 请求 + 1 次 PubChem 请求）。

### 幂等性

脚本可重复运行，**已存在的元素自动跳过**，不会产生重复数据。可用于增量补全 PubChem 缺失记录。

---

## 数据库表结构

### 表 1：Elements_Master（元素基础信息主表）

存储每个元素的核心标识、周期定位、物理状态等基础数据。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `Symbol` | VARCHAR(3) | 元素符号，主键（如 Fe、U、Og） |
| `Z` | INTEGER | 原子序数，唯一（1~118） |
| `Name_CN` | VARCHAR(10) | 中文名称（如 铁、铀、鿫） |
| `Name_EN` | VARCHAR(20) | 英文名称（如 Iron、Uranium） |
| `Ar` | REAL | 标准原子量（IUPAC 2021） |
| `MassNumber_MostStable` | INTEGER | 最稳定同位素质量数 |
| `Period` | INTEGER | 周期数（1~7） |
| `Group` | INTEGER | IUPAC 族序数（1~18） |
| `Group_Traditional` | VARCHAR(5) | 传统族序（如 IA、VIB、VIII） |
| `Block` | CHAR(1) | 电子区块（s/p/d/f） |
| `Category` | VARCHAR(20) | 元素大类 |
| `Subcategory` | VARCHAR(30) | 细分类别（如碱金属、卤素、镧系） |
| `ElectronConfig_Full` | VARCHAR(50) | 完整电子排布 |
| `ElectronConfig_Simplified` | VARCHAR(50) | 简化电子排布（稀有气体核心表示） |
| `ValenceElectrons` | VARCHAR(20) | 价层电子构型 |
| `UnpairedElectrons` | INTEGER | 基态未成对电子数 |
| `Phase_STP` | VARCHAR(10) | 标准温压相态（Solid/Liquid/Gas） |
| `CrystalStructure` | VARCHAR(20) | 晶体结构类型 |
| `MeltingPoint_K` | REAL | 熔点（开尔文） |
| `BoilingPoint_K` | REAL | 沸点（开尔文） |
| `Density_gcm3` | REAL | 标准态密度（g/cm³） |
| `ExamFrequency` | VARCHAR(10) | 考试频率（High/Medium/Low） |
| `KeyExamPoints` | TEXT | 考点关键词 |
| `Data_Source` | VARCHAR(50) | 数据来源 |
| `Data_Status` | VARCHAR(20) | 数据状态（Active/Deprecated/Superseded） |
| `Date_Collected` | DATE | 采集日期 |
| `Notes` | TEXT | 备注 |

**索引**：Period、Group、Block、Category

---

### 表 2：Properties_Periodic（周期性参数子表）

存储元素的周期性趋势数据，与 Elements_Master 通过 Symbol + Z 关联。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `id` | INTEGER | 自增主键 |
| `Symbol` | VARCHAR(3) | 元素符号，外键 |
| `Z` | INTEGER | 原子序数，外键 |
| `CovalentRadius_pm` | REAL | 共价半径（pm） |
| `MetallicRadius_pm` | REAL | 金属半径（pm） |
| `VanderWaalsRadius_pm` | REAL | 范德华半径（pm） |
| `IE1_kJmol` | REAL | 第一电离能（kJ/mol） |
| `IE2_kJmol` | REAL | 第二电离能（kJ/mol） |
| `IE3_kJmol` | REAL | 第三电离能（kJ/mol） |
| `IE4_kJmol` | REAL | 第四电离能（kJ/mol） |
| `EA_kJmol` | REAL | 电子亲和能（kJ/mol，**从 PubChem 补充**） |
| `Electronegativity_Pauling` | REAL | Pauling 电负性 |
| `Electronegativity_Mulliken` | REAL | Mulliken 电负性（**从 PubChem 补充**） |
| `Electronegativity_AllredRochow` | REAL | Allred-Rochow 电负性 |
| `E0_Acidic_V` | REAL | 酸性介质标准电极电势（V） |
| `E0_Basic_V` | REAL | 碱性介质标准电极电势（V） |
| `LatimerDiagram` | TEXT | Latimer 图数据 |
| `OxidationStates_Common` | TEXT | 常见氧化态 |
| `PubChem_Ar` | REAL | PubChem 原子量（用于质量对比） |
| `PubChem_EN` | REAL | PubChem Pauling 电负性（用于质量对比） |
| `PubChem_IE1_kJmol` | REAL | PubChem 第一电离能 kJ/mol（用于质量对比） |
| `Quality_Score` | INTEGER | **质量评分 0~3**，基于与 PubChem 一致性 |
| `Data_Discrepancy` | TEXT | 与 PubChem 差异描述 |
| `Data_Source` | VARCHAR(50) | 数据来源 |
| `Data_Status` | VARCHAR(20) | 数据状态 |
| `Date_Collected` | DATE | 采集日期 |
| `Notes` | TEXT | 备注 |

**索引**：Symbol

---

### 表 3：Properties_PubChem（PubChem 原始数据快照）

存储从 PubChem PUG REST API `rest/pug_view/data/element/{Z}/JSON` 抓取的原始数据，用于离线对比和补充。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `id` | INTEGER | 自增主键 |
| `Symbol` | VARCHAR(3) | 元素符号 |
| `Z` | INTEGER | 原子序数 |
| `Ar_Exact` | REAL | 精确原子量 |
| `Ar_Uncertainty` | REAL | 原子量不确定度 |
| `CovalentRadius_pm` | REAL | 共价半径 |
| `VanderWaalsRadius_pm` | REAL | 范德华半径 |
| `IE1_eV` ~ `IE4_eV` | REAL | 电离能（eV） |
| `EN_Pauling` | REAL | Pauling 电负性 |
| `EN_Mulliken` | REAL | Mulliken 电负性 |
| `EA_eV` | REAL | 电子亲和能（eV） |
| `OxidationStates` | TEXT | 氧化态列表 |
| `Phase_STP` | VARCHAR(10) | 标准相态 |
| `Density_gcm3` | REAL | 密度 |
| `MeltingPoint_K` | REAL | 熔点 |
| `BoilingPoint_K` | REAL | 沸点 |
| `ElectronConfig_Raw` | VARCHAR(100) | 原始电子排布字符串 |
| `ElectronConfig_Markup` | TEXT | **上标格式化电子排布**（如 `[He]2s²2p⁴`） |
| `ScrapedAt` | DATE | 抓取时间 |

**索引**：Symbol

---

## 质量评分系统（Quality_Score）

通过对比 Wikipedia 数据与 PubChem 权威数据，计算质量评分：

| 分值 | 条件 |
|------|------|
| **3** | Ar ±0.001 **且** EN ±0.1 **且** IE1 ±10 kJ/mol 均满足 |
| **2** | 满足其中 2 项 |
| **1** | 满足其中 1 项 |
| **0** | 全部不满足，或 PubChem 无该数据 |

**当前分布**（118 元素）：
- Score 3：66 个（数据高度一致）
- Score 2：28 个（部分差异）
- Score 1：7 个
- Score 0：17 个（主要是锕系/超重元素，PubChem 本身数据有限）

---

## 常用查询示例

```bash
# 查看所有元素基础信息
sqlite3 periodic_table.db "SELECT Symbol, Z, Name_CN, Name_EN, Ar, Period, Block FROM Elements_Master ORDER BY Z;"

# 查询氧的完整数据
sqlite3 periodic_table.db "SELECT * FROM Elements_Master WHERE Symbol='O';"
sqlite3 periodic_table.db "SELECT * FROM Properties_Periodic WHERE Symbol='O';"
sqlite3 periodic_table.db "SELECT * FROM Properties_PubChem WHERE Symbol='O';"

# 查看质量评分分布
sqlite3 periodic_table.db "SELECT Quality_Score, COUNT(*) as cnt FROM Properties_Periodic GROUP BY Quality_Score ORDER BY Quality_Score;"

# 查询第一电离能最高的 10 个元素
sqlite3 periodic_table.db "SELECT e.Symbol, e.Name_CN, p.IE1_kJmol FROM Elements_Master e JOIN Properties_Periodic p ON e.Symbol=p.Symbol WHERE p.IE1_kJmol IS NOT NULL ORDER BY p.IE1_kJmol DESC LIMIT 10;"

# 查询所有过渡金属
sqlite3 periodic_table.db "SELECT Symbol, Name_CN, Ar, Period, Group FROM Elements_Master WHERE Block='d' ORDER BY Z;"

# 查询标准状态下为气体的元素
sqlite3 periodic_table.db "SELECT Symbol, Name_CN, Phase_STP FROM Elements_Master WHERE Phase_STP='Gas' ORDER BY Z;"

# 查询 EA（电子亲和能）从 PubChem 补充的元素
sqlite3 periodic_table.db "SELECT e.Symbol, e.Name_CN, p.EA_kJmol FROM Elements_Master e JOIN Properties_Periodic p ON e.Symbol=p.Symbol WHERE p.EA_kJmol IS NOT NULL ORDER BY e.Z;"

# 查看 Score 0 的元素（数据存疑）
sqlite3 periodic_table.db "SELECT e.Symbol, e.Name_CN, p.Quality_Score, p.Data_Discrepancy FROM Elements_Master e JOIN Properties_Periodic p ON e.Symbol=p.Symbol WHERE p.Quality_Score=0;"
```

---

## 数据来源与质量

| 优先级 | 来源 | 用途 |
|--------|------|------|
| P1 | 中文维基百科 | 中文名称、基础物理属性 |
| P2 | 英文维基百科 | 英文名称、详细周期参数、电离能、半径 |
| P3 | PubChem PUG REST API | 交叉验证、补充 EA 和 Mulliken 电负性 |

**数据覆盖情况**：

| 表 | 记录数 | 覆盖率 |
|----|--------|--------|
| Elements_Master | 118/118 | 100% |
| Properties_Periodic | 118/118 | 100% |
| Properties_PubChem | 118/118 | 100% |

---

## 设计文档

详细的数据库设计规范见 `periodic_table_of_elements_sqlite_design.md`，包含：
- 字段命名规范与数据类型标准
- 三表关联结构设计（主表、属性子表、PubChem 快照表）
- PubChem 交叉验证与质量评分机制
- 元素化学属性体系（原子参数、周期趋势、电化学数据等）

> 当前实现包含完整三表。反应子表（Reactions_Characteristic）和考点关联表（ExamPoints_Mapping）为预留扩展。

---

## 注意事项

1. **网络依赖**：首次运行必须联网抓取维基百科和 PubChem 数据
2. **抓取速率**：内置约 1.2 秒延迟，避免对维基百科服务器造成压力
3. **数据时效**：维基百科数据可能随时间更新，定期重建可获取最新数据
4. **中文字符**：部分超重元素（Z>104）的中文名使用 Unicode 扩展字符，确保终端支持 UTF-8
5. **PubChem 覆盖**：所有 118 元素均已获取 PubChem 记录，Score 0 主要出现在锕系和超重元素（Z≥89），因 PubChem 本身数据稀缺或不精确
