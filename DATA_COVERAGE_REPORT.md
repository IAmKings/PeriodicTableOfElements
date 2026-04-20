# 元素周期表数据库 — 数据覆盖统计报告

> 生成时间：2026-04-20
> 数据库版本：v1.0.0
> 元素总数：118
> 数据来源：中文维基百科 / 英文维基百科 / PubChem API（补充）

---

## 一、总体概览

| 指标 | 数值 |
|------|------|
| 总元素数 | 118 |
| 主表记录数 | 118（100%） |
| 属性表记录数 | 118（100%） |
| 无任何缺失的核心字段 | Symbol, Z, Name_CN, Name_EN, Period, "Group", Block, Phase_STP, ElectronConfig |

---

## 二、Elements_Master（元素基础信息主表）字段覆盖详情

| 字段 | 类型 | 已填充 | 缺失 | 覆盖率 | 备注 |
|------|------|--------|------|--------|------|
| Symbol | VARCHAR(3) | 118 | 0 | **100.0%** | 主键，全部正确 |
| Z | INTEGER | 118 | 0 | **100.0%** | 原子序数 |
| Name_CN | VARCHAR(10) | 118 | 0 | **100.0%** | 中文名 |
| Name_EN | VARCHAR(20) | 118 | 0 | **100.0%** | 英文名 |
| Ar | REAL | 111 | 7 | **94.1%** | 标准原子量 |
| Period | INTEGER | 118 | 0 | **100.0%** | 周期数 |
| "Group" | INTEGER | 118 | 0 | **100.0%** | IUPAC 族序 |
| Group_Traditional | VARCHAR(5) | 118 | 0 | **100.0%** | 传统族序 |
| Block | CHAR(1) | 118 | 0 | **100.0%** | 电子区块 |
| Category | VARCHAR(20) | 0 | 118 | **0.0%** | 元素大类 |
| Subcategory | VARCHAR(30) | 118 | 0 | **100.0%** | 细分类别（脚本推导） |
| ElectronConfig_Full | VARCHAR(50) | 118 | 0 | **100.0%** | 完整电子排布 |
| ElectronConfig_Simplified | VARCHAR(50) | 118 | 0 | **100.0%** | 简化电子排布 |
| ValenceElectrons | VARCHAR(20) | 118 | 0 | **100.0%** | 价层电子（脚本推导） |
| UnpairedElectrons | INTEGER | 0 | 118 | **0.0%** | 未成对电子数 |
| Phase_STP | VARCHAR(10) | 118 | 0 | **100.0%** | 标准相态 |
| CrystalStructure | VARCHAR(20) | 114 | 4 | **96.6%** | 晶体结构 |
| MeltingPoint_K | REAL | 108 | 10 | **91.5%** | 熔点 |
| BoilingPoint_K | REAL | 104 | 14 | **88.1%** | 沸点 |
| Density_gcm3 | REAL | 10 | 108 | **8.5%** | 密度 |
| ExamFrequency | VARCHAR(10) | 0 | 118 | **0.0%** | 考试频率 |
| KeyExamPoints | TEXT | 0 | 118 | **0.0%** | 考点关键词 |
| Data_Source | VARCHAR(50) | 118 | 0 | **100.0%** | 数据来源 |
| Date_Collected | DATE | 118 | 0 | **100.0%** | 采集日期 |

### Elements_Master 缺失明细

| 字段 | 缺失元素 | 缺失原因 |
|------|----------|----------|
| Ar | Fm, Ds, Rg, Cn, Nh, Mc, Ts | 超重元素无稳定同位素，标准原子量未确定 |
| CrystalStructure | Fl, Mc, Lv, Ts | 超重元素实验数据极少，晶体结构未知 |
| MeltingPoint_K | He, C, As, Db, Sg, Bh, Hs, Mt, Ds, Rg | He 无标准熔点（需加压）；C/As 直接升华；超重元素实验数据缺失 |
| BoilingPoint_K | C, As, Fm, Md, No, Lr, Db, Sg, Bh, Hs, Mt, Ds, Rg, Fl | C/As 直接升华无沸点；锕系/超重元素实验数据缺失 |
| Density_gcm3 | Li, Be, B, C, Na, Mg, Al, Si, P, S, K, Ca, Sc...（共 108 个） | 维基百科 infobox 密度字段格式不统一，解析逻辑未能覆盖大部分格式 |
| Category | 全部 118 个 | 英文维基百科 "element category" 字段实际值为空或解析未命中 |
| UnpairedElectrons | 全部 118 个 | 推导逻辑基于 ValenceElectrons，但正则表达式未匹配到有效数据 |
| ExamFrequency / KeyExamPoints | 全部 118 个 | 未实现抓取逻辑，需人工标注或基于题库数据填充 |

---

## 三、Properties_Periodic（周期性参数子表）字段覆盖详情

| 字段 | 类型 | 已填充 | 缺失 | 覆盖率 | 备注 |
|------|------|--------|------|--------|------|
| Symbol | VARCHAR(3) | 118 | 0 | **100.0%** | 外键 |
| Z | INTEGER | 118 | 0 | **100.0%** | 外键 |
| CovalentRadius_pm | REAL | 109 | 9 | **92.4%** | 共价半径 |
| MetallicRadius_pm | REAL | 0 | 118 | **0.0%** | 金属半径 |
| VanderWaalsRadius_pm | REAL | 55 | 63 | **46.6%** | 范德华半径 |
| IE1_kJmol | REAL | 117 | 1 | **99.2%** | 第一电离能 |
| IE2_kJmol | REAL | 102 | 16 | **86.4%** | 第二电离能 |
| IE3_kJmol | REAL | 90 | 28 | **76.3%** | 第三电离能 |
| IE4_kJmol | REAL | 0 | 118 | **0.0%** | 第四电离能 |
| EA_kJmol | REAL | 0 | 118 | **0.0%** | 电子亲和能 |
| Electronegativity_Pauling | REAL | 100 | 18 | **84.7%** | Pauling 电负性 |
| Electronegativity_Mulliken | REAL | 0 | 118 | **0.0%** | Mulliken 电负性 |
| Electronegativity_AllredRochow | REAL | 0 | 118 | **0.0%** | Allred-Rochow 电负性 |
| E0_Acidic_V | REAL | 0 | 118 | **0.0%** | 酸性标准电极电势 |
| E0_Basic_V | REAL | 0 | 118 | **0.0%** | 碱性标准电极电势 |
| LatimerDiagram | TEXT | 0 | 118 | **0.0%** | Latimer 图数据 |
| OxidationStates_Common | TEXT | 118 | 0 | **100.0%** | 常见氧化态 |
| Data_Source | VARCHAR(50) | 118 | 0 | **100.0%** | 数据来源 |
| Date_Collected | DATE | 118 | 0 | **100.0%** | 采集日期 |

### Properties_Periodic 缺失明细

| 字段 | 缺失元素 | 缺失原因 |
|------|----------|----------|
| CovalentRadius_pm | Hg, At, Bk, Cf, Es, Fm, Md, No, Lr | 维基百科未提供或格式无法解析（如汞以金属半径为主） |
| VanderWaalsRadius_pm | Ti, V, Cr, Mn, Co, Y, Zr, Nb, Mo, Ru, Rh, La~Lu 系列, Hf~Ir 系列, Hg, At, Ac~Lr 系列, Rf~Og | 范德华半径主要定义于非金属元素，过渡金属和 f 区元素大多未在维基百科标注 |
| IE1_kJmol | Hg | 英文维基百科汞页面 infobox 中 "Ionisation energies" 字段解析未命中（可能字段名不同或格式特殊） |
| IE2_kJmol | H, Hg, Po, At, Rn, Fr, Pa, Np, Pu, Am, Cm, Bk, Cf, Es, Fm, Md | 氢无第二电离能；部分元素英文维基未列出多级电离能 |
| IE3_kJmol | H, He, Li, Hg, Po~Md 等 | 前几个元素电离能级数不足；部分元素未列出三级以上 |
| Electronegativity_Pauling | He, Ne, Ar, Rf~Og（共 18 个） | 稀有气体电负性无定义；超重元素实验数据极少 |
| EA_kJmol | 全部 118 个 | 英文维基百科 infobox 中 "electron affinity" 字段解析未命中（字段名/格式问题） |
| MetallicRadius_pm | 全部 118 个 | 未实现抓取逻辑（维基百科中 "atomic radius" / "metallic radius" 字段格式多样） |
| Electronegativity_Mulliken / AllredRochow | 全部 118 个 | 维基百科 infobox 通常只列出 Pauling 电负性，其他标度未直接提供 |
| E0_Acidic_V / E0_Basic_V | 全部 118 个 | 未实现抓取逻辑（需从 Latimer 图或专门电化学页面提取） |
| LatimerDiagram | 全部 118 个 | 未实现抓取逻辑（维基百科中 Latimer 图以图片形式存在，无结构化文本） |
| IE4_kJmol | 全部 118 个 | 英文维基百科 infobox 通常只列出 1~3 级电离能 |

---

## 四、问题分级与优化优先级

### P0 — 高优先级（影响核心可用性）

| 问题 | 影响范围 | 建议修复方案 |
|------|----------|-------------|
| Category 全部为空 | 118 个元素 | 检查英文维基 "element category" 字段名，可能为 "category" 或在新版 infobox 中位置变化；或改为基于 Subcategory + Block 推导 |
| Density_gcm3 仅 8.5% | 108 个元素 | 维基百科密度字段格式极多（"1.25 g/L", "7.87 g/cm³", "0.808 g/cm³ (at b.p.)" 等），需增强 `_parse_density` 正则表达式 |
| UnpairedElectrons 全部为空 | 118 个元素 | 推导逻辑中 `ValenceElectrons` 的正则表达式 `r"(\d)([spdf])\^?(\d+)"` 未匹配到数据；需检查实际存储的格式 |

### P1 — 中优先级（重要数据可提升）

| 问题 | 影响范围 | 建议修复方案 |
|------|----------|-------------|
| EA_kJmol 全部为空 | 118 个元素 | 检查英文维基 infobox 字段名，可能是 "electron affinity" 或缩写 "EA"；部分元素为负值/正值需正确处理 |
| IE4_kJmol 全部为空 | 118 个元素 | 英文维基通常只列 1~3 级；可从专门数据源（如 NIST）获取 |
| MetallicRadius_pm 全部为空 | 118 个元素 | 维基百科中金属半径与原子半径混在一起；需针对性解析 "atomic radius: empirical: 126 pm" 等格式 |
| IE1 缺失（Hg） | 1 个元素 | 单独检查汞的英文维基 infobox 结构 |
| Electronegativity_Mulliken / AllredRochow | 118 个元素 | 维基百科 infobox 无此字段；需从专门化学数据库（如 WebElements）抓取或手动维护映射表 |

### P2 — 低优先级（扩展数据）

| 问题 | 影响范围 | 建议修复方案 |
|------|----------|-------------|
| E0_Acidic_V / E0_Basic_V | 118 个元素 | 未在维基百科 infobox 中直接提供；可从每个元素的 " aqueous chemistry" 段落或专门电化学表中提取 |
| LatimerDiagram | 118 个元素 | 维基百科中通常为图片；需 OCR 或从文本描述中重建 |
| ExamFrequency / KeyExamPoints | 118 个元素 | 无自动数据源；需人工基于题库/考试大纲标注 |
| IE2/IE3 部分缺失 | 16~28 个元素 | 前几个元素（H, He, Li）自然缺失；其余可从 NIST 补充 |

---

## 五、数据来源质量说明

### 已正确抓取的数据源

- **中文维基百科**：中文名称、基础物理属性（熔点/沸点格式较统一）
- **英文维基百科**：原子量、周期定位（Group/Period/Block）、电子构型、电离能（1~3级）、Pauling电负性、共价半径、氧化态

### 抓取失败的常见原因

1. **字段名差异**：英式拼写 "Ionisation" vs 美式 "Ionization"（已部分修复）
2. **Infobox 结构差异**：部分元素使用新版 infobox（`ib-element` 类），字段位置变化
3. ** colspan 表头**：如 "Standard atomic weight" 占用整行，数据在下一行（已修复）
4. **数值格式多样**：密度有 g/cm³、g/L、"predicted"、括号注释等
5. **定义性缺失**：稀有气体无电负性；范德华半径对金属元素通常未定义
6. **超重元素**：Z≥104 的元素实验数据极少，大部分属性为理论预测值

---

## 六、后续优化 checklist

- [ ] 修复 Category 字段抓取（P0）
- [ ] 修复 Density_gcm3 解析逻辑，覆盖更多格式（P0）
- [ ] 修复 UnpairedElectrons 推导逻辑（P0）
- [ ] 修复 EA_kJmol 抓取（检查字段名/格式）（P1）
- [ ] 修复 IE1 缺失（Hg）（P1）
- [ ] 补充 MetallicRadius_pm 抓取（P1）
- [ ] 从 NIST/WebElements 补充 IE4、Mulliken/Allred-Rochow 电负性（P1）
- [ ] 从 NIST 补充电子亲和能数据（P1）
- [ ] 设计 ExamFrequency/KeyExamPoints 标注方案（P2）
- [ ] 从电化学专题页面抓取 E0 数据（P2）
- [ ] 评估是否引入 PubChem / ChemSpider / WebElements 作为结构化数据源（P2）

---

*本报告基于 `periodic_table.db` 实际数据生成，用于指导后续数据质量优化工作。*
