# 元素周期表 SQLite 数据库

基于维基百科（中/英文）和 PubChem API 抓取的 118 种化学元素数据，生成 SQLite 关系型数据库。

---

## 项目结构

```
.
├── periodic_table.db              # 生成的 SQLite 数据库
├── periodic_table_of_elements_sqlite_design.md  # 数据库设计文档
├── scripts/
│   ├── build_db.py                # 主抓取脚本
│   ├── schema.sql                 # 数据库表结构定义
│   ├── element_list.json          # 118 元素基础清单
│   └── requirements.txt           # Python 依赖
└── README.md                      # 本文件
```

---

## 环境要求

- Python 3.11+
- 网络连接（用于抓取维基百科和 PubChem）

---

## 安装依赖

```bash
cd scripts
pip install -r requirements.txt
```

依赖包：
- `requests` — HTTP 请求
- `beautifulsoup4` — HTML 解析
- `lxml` — BeautifulSoup 的解析后端

---

## 运行脚本

### 生成/重建数据库

```bash
python3 scripts/build_db.py
```

脚本行为：
1. 自动删除旧的 `periodic_table.db`（如果存在）
2. 按原子序数 Z=1~118 逐个抓取
3. 每个元素先访问**中文维基百科**获取中文名和基础属性
4. 再访问**英文维基百科**补充详细数据
5. 尝试调用 **PubChem API** 获取补充数据
6. 自动推导子分类、传统族序、价电子、未成对电子数等派生字段
7. 写入 SQLite 数据库
8. 抓取间隔约 1.2 秒，避免对维基百科服务器造成压力

总耗时约 **10~12 分钟**（118 个元素 × 2~3 次请求）。

### 幂等性

脚本可重复运行，每次都会重新创建数据库，不会产生重复数据。

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

存储元素的周期性趋势数据，与主表通过 `Symbol` + `Z` 关联。

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
| `EA_kJmol` | REAL | 电子亲和能（kJ/mol） |
| `Electronegativity_Pauling` | REAL | Pauling 电负性 |
| `Electronegativity_Mulliken` | REAL | Mulliken 电负性 |
| `Electronegativity_AllredRochow` | REAL | Allred-Rochow 电负性 |
| `E0_Acidic_V` | REAL | 酸性介质标准电极电势（V） |
| `E0_Basic_V` | REAL | 碱性介质标准电极电势（V） |
| `LatimerDiagram` | TEXT | Latimer 图数据 |
| `OxidationStates_Common` | TEXT | 常见氧化态 |
| `Data_Source` | VARCHAR(50) | 数据来源 |
| `Data_Status` | VARCHAR(20) | 数据状态 |
| `Date_Collected` | DATE | 采集日期 |
| `Notes` | TEXT | 备注 |

**索引**：Symbol

---

## 常用查询示例

```bash
# 查看所有元素基础信息
sqlite3 periodic_table.db "SELECT Symbol, Z, Name_CN, Name_EN, Ar, Period, Block FROM Elements_Master ORDER BY Z;"

# 查询铁的详细属性
sqlite3 periodic_table.db "SELECT * FROM Elements_Master WHERE Symbol = 'Fe';"

# 查询第一电离能最高的 10 个元素
sqlite3 periodic_table.db "SELECT e.Symbol, e.Name_CN, p.IE1_kJmol FROM Elements_Master e JOIN Properties_Periodic p ON e.Symbol = p.Symbol WHERE p.IE1_kJmol IS NOT NULL ORDER BY p.IE1_kJmol DESC LIMIT 10;"

# 查询所有过渡金属
sqlite3 periodic_table.db "SELECT Symbol, Name_CN, Ar, Period, Group FROM Elements_Master WHERE Block = 'd' ORDER BY Z;"

# 查询标准状态下为气体的元素
sqlite3 periodic_table.db "SELECT Symbol, Name_CN, Phase_STP FROM Elements_Master WHERE Phase_STP = 'Gas' ORDER BY Z;"
```

---

## 数据来源与质量

| 优先级 | 来源 | 用途 |
|--------|------|------|
| P1 | 中文维基百科 | 中文名称、基础物理属性 |
| P2 | 英文维基百科 | 英文名称、详细周期参数、电离能、半径 |
| P3 | PubChem API | 补充验证（部分元素因网络问题未获取） |

### 数据覆盖情况

**Elements_Master**
- 原子序数/符号/名称：118/118（100%）
- 周期/族/区块：118/118（100%）
- 电子构型：118/118（100%）
- 标准原子量：111/118（94%，超重元素部分为理论值）
- 熔点/沸点：108/118 / 104/118
- 晶体结构：114/118（97%）

**Properties_Periodic**
- Pauling 电负性：100/118（85%）
- 第一电离能：117/118（99%，仅汞缺失）
- 共价半径：109/118（92%）
- 范德华半径：55/118（47%，部分元素未定义）

---

## 设计文档

详细的数据库设计规范见 `periodic_table_of_elements_sqlite_design.md`，包含：
- 字段命名规范与数据类型标准
- 四表关联结构设计（主表、属性子表、反应子表、考点关联表）
- 数据来源与交叉验证机制
- 元素化学属性体系（原子参数、周期趋势、电化学数据等）

> 当前实现仅包含主表和属性子表。反应子表（Reactions_Characteristic）和考点关联表（ExamPoints_Mapping）为预留扩展。

---

## 注意事项

1. **网络依赖**：首次运行必须联网抓取维基百科数据
2. **抓取速率**：内置 1.2 秒延迟，请勿修改过快以免被封
3. **数据时效**：维基百科数据可能随时间更新，建议定期重建数据库
4. **中文字符**：部分超重元素（Z>104）的中文名使用 Unicode 扩展字符，确保终端支持 UTF-8
