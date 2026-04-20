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
| 数据表数量 | 3（Elements_Master / Properties_Periodic / Properties_PubChem） |
| 主表记录数 | 118（100%） |
| 属性表记录数（两张）| 118 / 118（100%） |
| 无任何缺失的核心字段 | Symbol, Z, Name_CN, Name_EN, Period, "Group", Block, Phase_STP, ElectronConfig_Full/Simplified, ValenceElectrons, Subcategory, ExamFrequency, KeyExamPoints |

---

## 二、Elements_Master（元素基础信息主表）字段覆盖详情

| 字段 | 类型 | 已填充 | 缺失 | 覆盖率 | 备注 |
|------|------|--------|------|--------|------|
| Symbol | VARCHAR(3) | 118 | 0 | **100.0%** | 主键，全部正确 |
| Z | INTEGER | 118 | 0 | **100.0%** | 原子序数 |
| Name_CN | VARCHAR(10) | 118 | 0 | **100.0%** | 中文名 |
| Name_EN | VARCHAR(20) | 118 | 0 | **100.0%** | 英文名 |
| Ar | REAL | 83 | 35 | **70.3%** | 标准原子量 |
| Period | INTEGER | 118 | 0 | **100.0%** | 周期数 |
| "Group" | INTEGER | 118 | 0 | **100.0%** | IUPAC 族序 |
| Group_Traditional | VARCHAR(5) | 118 | 0 | **100.0%** | 传统族序 |
| Block | CHAR(1) | 118 | 0 | **100.0%** | 电子区块 |
| Category | VARCHAR(20) | 0 | 118 | **0.0%** | 元素大类（待修复） |
| Subcategory | VARCHAR(30) | 118 | 0 | **100.0%** | 细分类别（脚本推导） |
| ElectronConfig_Full | VARCHAR(50) | 118 | 0 | **100.0%** | 完整电子排布 |
| ElectronConfig_Simplified | VARCHAR(50) | 118 | 0 | **100.0%** | 简化电子排布 |
| ValenceElectrons | VARCHAR(20) | 118 | 0 | **100.0%** | 价层电子（脚本推导） |
| UnpairedElectrons | INTEGER | 0 | 118 | **0.0%** | 未成对电子数（待修复） |
| Phase_STP | VARCHAR(10) | 118 | 0 | **100.0%** | 标准相态 |
| CrystalStructure | VARCHAR(20) | 114 | 4 | **96.6%** | 晶体结构 |
| MeltingPoint_K | REAL | 108 | 10 | **91.5%** | 熔点 |
| BoilingPoint_K | REAL | 104 | 14 | **88.1%** | 沸点 |
| Density_gcm3 | REAL | 10 | 108 | **8.5%** | 密度（解析逻辑待增强） |
| ExamFrequency | VARCHAR(10) | 118 | 0 | **100.0%** | 考试频率 |
| KeyExamPoints | TEXT | 118 | 0 | **100.0%** | 考点关键词 |
| Data_Source | VARCHAR(50) | 118 | 0 | **100.0%** | 数据来源 |
| Date_Collected | DATE | 118 | 0 | **100.0%** | 采集日期 |

### Elements_Master 缺失明细

| 字段 | 缺失元素 | 缺失原因 |
|------|----------|----------|
| Ar | Tc, Pm, Hg, Po, At, Rn, Fr, Ra, Ac, Np, Pu, Am, Cm, Bk, Cf, Es, Fm, Md, No, Lr, Rf, Db, Sg, Bh, Hs, Mt, Ds, Rg, Cn, Nh, Fl, Mc, Lv, Ts, Og（共 35 个） | 无稳定同位素的放射性元素（Tc, Pm, Po, At, Rn, Fr, Ra 等）及超重元素（Z≥104），IUPAC 未定义标准原子量 |
| CrystalStructure | Fl, Mc, Lv, Ts | 超重元素实验数据极少，晶体结构未知 |
| MeltingPoint_K | He, C, As, Db, Sg, Bh, Hs, Mt, Ds, Rg | He 无标准熔点（需加压）；C/As 直接升华；超重元素实验数据缺失 |
| BoilingPoint_K | C, As, Fm, Md, No, Lr, Db, Sg, Bh, Hs, Mt, Ds, Rg, Fl | C/As 直接升华无沸点；锕系末尾及超重元素实验数据缺失 |
| Density_gcm3 | 共 108 个 | 维基百科密度字段格式极不统一（"1.25 g/L"、"7.87 g/cm³"、"predicted"、括号注释等），`_parse_density` 正则表达式覆盖不足 |
| Category | 全部 118 个 | 英文维基 infobox "element category" 字段解析未命中（字段名或位置在新版 infobox 中变化） |
| UnpairedElectrons | 全部 118 个 | 推导正则 `r"(\d)([spdf])\^?(\d+)"` 未匹配 `ValenceElectrons` 实际存储格式 |

---

## 三、Properties_Periodic（周期性参数子表）字段覆盖详情

| 字段 | 类型 | 已填充 | 缺失 | 覆盖率 | 备注 |
|------|------|--------|------|--------|------|
| Symbol | VARCHAR(3) | 118 | 0 | **100.0%** | 外键 |
| Z | INTEGER | 118 | 0 | **100.0%** | 外键 |
| CovalentRadius_pm | REAL | 109 | 9 | **92.4%** | 共价半径 |
| MetallicRadius_pm | REAL | 0 | 118 | **0.0%** | 金属半径（未实现） |
| VanderWaalsRadius_pm | REAL | 55 | 63 | **46.6%** | 范德华半径 |
| IE1_kJmol | REAL | 117 | 1 | **99.2%** | 第一电离能 |
| IE2_kJmol | REAL | 102 | 16 | **86.4%** | 第二电离能 |
| IE3_kJmol | REAL | 90 | 28 | **76.3%** | 第三电离能 |
| IE4_kJmol | REAL | 0 | 118 | **0.0%** | 第四电离能（未实现） |
| EA_kJmol | REAL | 75 | 43 | **63.6%** | 电子亲和能 |
| Electronegativity_Pauling | REAL | 100 | 18 | **84.7%** | Pauling 电负性 |
| Electronegativity_Mulliken | REAL | 69 | 49 | **58.5%** | Mulliken 电负性 |
| Electronegativity_AllredRochow | REAL | 0 | 118 | **0.0%** | Allred-Rochow 电负性（未实现） |
| E0_Acidic_V | REAL | 0 | 118 | **0.0%** | 酸性标准电极电势（未实现） |
| E0_Basic_V | REAL | 0 | 118 | **0.0%** | 碱性标准电极电势（未实现） |
| LatimerDiagram | TEXT | 0 | 118 | **0.0%** | Latimer 图数据（未实现） |
| OxidationStates_Common | TEXT | 118 | 0 | **100.0%** | 常见氧化态 |
| Data_Source | VARCHAR(50) | 118 | 0 | **100.0%** | 数据来源 |
| Date_Collected | DATE | 118 | 0 | **100.0%** | 采集日期 |

### Properties_Periodic 缺失明细

| 字段 | 缺失元素 | 缺失原因 |
|------|----------|----------|
| CovalentRadius_pm | Hg, At, Bk, Cf, Es, Fm, Md, No, Lr | 维基百科未提供或格式无法解析（汞以金属半径为主） |
| VanderWaalsRadius_pm | 63 个（主要为过渡金属、f 区元素及超重元素） | 范德华半径主要定义于非金属元素，过渡金属和 f 区元素大多未在维基百科标注 |
| IE1_kJmol | Hg | 英文维基汞页面 infobox 字段名/格式特殊，解析未命中 |
| IE2_kJmol | H, Hg, Po, At, Rn, Fr, Pa, Np, Pu, Am, Cm, Bk, Cf, Es, Fm, Md（共 16 个） | 氢无第二电离能；部分元素英文维基未列出多级电离能 |
| IE3_kJmol | H, He, Li, Hg, Po~Md 等（共 28 个） | 前几个元素级数不足；部分元素未列出三级以上 |
| EA_kJmol | 43 个 | 稀有气体、部分超重元素无实测值；其余为维基百科格式解析遗漏 |
| Electronegativity_Pauling | He, Ne, Ar, Rf, Db, Sg, Bh, Hs, Mt, Ds, Rg, Cn, Nh, Fl, Mc, Lv, Ts, Og（共 18 个） | 稀有气体无定义；超重元素实验数据极少 |
| Electronegativity_Mulliken | 49 个 | 维基百科 infobox 通常只列 Pauling 值；其余通过衍生公式计算，数据源覆盖不完整 |
| MetallicRadius_pm | 全部 118 个 | 未实现抓取逻辑（维基百科 "atomic radius / metallic radius" 字段格式多样） |
| Electronegativity_AllredRochow | 全部 118 个 | 维基百科 infobox 无此字段，需从专门化学数据库获取 |
| E0_Acidic_V / E0_Basic_V | 全部 118 个 | 未实现抓取逻辑（需从 Latimer 图或专门电化学页面提取） |
| LatimerDiagram | 全部 118 个 | 维基百科中以图片形式存在，无结构化文本 |
| IE4_kJmol | 全部 118 个 | 英文维基 infobox 通常只列 1~3 级电离能 |

---

## 四、Properties_PubChem（PubChem 补充数据表）字段覆盖详情

| 字段 | 类型 | 已填充 | 缺失 | 覆盖率 | 备注 |
|------|------|--------|------|--------|------|
| Symbol | VARCHAR(3) | 118 | 0 | **100.0%** | 外键 |
| Z | INTEGER | 118 | 0 | **100.0%** | 外键 |
| Ar_Exact | REAL | 118 | 0 | **100.0%** | 精确原子量（PubChem） |
| Ar_Uncertainty | REAL | 0 | 118 | **0.0%** | 原子量不确定度（未抓取） |
| CovalentRadius_pm | REAL | 87 | 31 | **73.7%** | 共价半径 |
| VanderWaalsRadius_pm | REAL | 99 | 19 | **83.9%** | 范德华半径 |
| IE1_eV | REAL | 108 | 10 | **91.5%** | 第一电离能（eV） |
| IE2_eV | REAL | 0 | 118 | **0.0%** | 第二电离能（未抓取） |
| IE3_eV | REAL | 0 | 118 | **0.0%** | 第三电离能（未抓取） |
| IE4_eV | REAL | 0 | 118 | **0.0%** | 第四电离能（未抓取） |
| EN_Pauling | REAL | 98 | 20 | **83.1%** | Pauling 电负性 |
| EN_Mulliken | REAL | 69 | 49 | **58.5%** | Mulliken 电负性 |
| EA_eV | REAL | 75 | 43 | **63.6%** | 电子亲和能（eV） |
| OxidationStates | TEXT | 117 | 1 | **99.2%** | 氧化态 |
| Phase_STP | VARCHAR(10) | 118 | 0 | **100.0%** | 标准相态 |
| Density_gcm3 | REAL | 96 | 22 | **81.4%** | 密度 |
| MeltingPoint_K | REAL | 108 | 10 | **91.5%** | 熔点 |
| BoilingPoint_K | REAL | 101 | 17 | **85.6%** | 沸点 |
| ElectronConfig_Raw | VARCHAR(100) | 117 | 1 | **99.2%** | 电子排布（原始） |
| ElectronConfig_Markup | TEXT | 117 | 1 | **99.2%** | 电子排布（标注格式） |

### Properties_PubChem 缺失明细

| 字段 | 缺失元素 | 缺失原因 |
|------|----------|----------|
| CovalentRadius_pm | He, Ne, Ar, Kr, Xe, At, Rn, Fr, Cm, Bk, Cf, Es, Fm, Md, No, Lr, Rf~Og（共 31 个） | 稀有气体通常无共价键数据；超重元素 PubChem 数据不完整 |
| VanderWaalsRadius_pm | Fm, Md, No, Lr, Rf~Og（共 19 个） | 超重元素 PubChem 无此数据 |
| IE1_eV | Mt, Ds, Rg, Cn, Nh, Fl, Mc, Lv, Ts, Og（共 10 个） | Z≥109 超重元素 PubChem 无电离能数据 |
| EN_Pauling | Pm, Eu, Tb, Yb, Lr, Rf~Og（共 20 个） | 部分镧系稀土及超重元素无 Pauling 电负性实测值 |
| Density_gcm3 | Fr, Cf, Es, Fm, Md, No, Lr, Rf~Og（共 22 个） | 超重元素及部分锕系末尾元素实验密度数据缺失 |
| IE2~IE4_eV | 全部 118 个 | PubChem API 抓取脚本仅实现了 IE1，其余级数未抓取 |
| Ar_Uncertainty | 全部 118 个 | 抓取脚本未解析此字段 |

---

## 五、跨表对比：同类字段覆盖率汇总

| 字段 | Properties_Periodic | Properties_PubChem | 差值 | 建议 |
|------|---------------------|--------------------|------|------|
| CovalentRadius_pm | 92.4%（109/118） | 73.7%（87/118） | PP 更优 +18.7% | 以 PP 为主，PubChem 补充 |
| VanderWaalsRadius_pm | 46.6%（55/118） | 83.9%（99/118） | PC 更优 +37.3% | **以 PubChem 为主** |
| IE1 | 99.2%（117/118，kJ/mol） | 91.5%（108/118，eV） | PP 更优 +7.7% | 以 PP 为主，单位注意换算（1 eV = 96.485 kJ/mol） |
| EA | 63.6%（75/118，kJ/mol） | 63.6%（75/118，eV） | 相同 | 两者可互补，优先校验一致性 |
| EN_Pauling | 84.7%（100/118） | 83.1%（98/118） | PP 略优 | 以 PP 为主 |
| EN_Mulliken | 58.5%（69/118） | 58.5%（69/118） | 相同 | 两表数据应一致，需校验 |
| Density_gcm3 | 8.5%（10/118） | **81.4%（96/118）** | PC 远优 +72.9% | **立即迁移 PubChem 密度数据至主表** |
| MeltingPoint_K | 91.5%（108/118） | 91.5%（108/118） | 相同 | 两者可交叉验证 |
| BoilingPoint_K | 88.1%（104/118） | 85.6%（101/118） | PP 略优 | 以 PP 为主 |

---

## 六、问题分级与优化优先级

### P0 — 高优先级（影响核心可用性）

| 问题 | 影响范围 | 建议修复方案 |
|------|----------|-------------|
| Category 全部为空 | 118 个元素 | 检查英文维基 "element category" 字段名变化；或基于 Subcategory + Block 推导填充 |
| Density_gcm3 仅 8.5% | 108 个元素 | **立即从 Properties_PubChem 迁移（96 个元素已有数据）**；剩余 22 个元素增强正则解析 |
| UnpairedElectrons 全部为空 | 118 个元素 | 检查 `ValenceElectrons` 实际存储格式并修复推导正则 |

### P1 — 中优先级（重要数据可提升）

| 问题 | 影响范围 | 建议修复方案 |
|------|----------|-------------|
| Ar 覆盖率降至 70.3% | 35 个元素缺失 | 对已有 `Ar_Exact`（PubChem 100%）的元素交叉回填；放射性元素标注 "无标准原子量" |
| VanderWaalsRadius_pm 仅 46.6%（PP） | 63 个元素 | 从 Properties_PubChem 补充（99/118，83.9%），直接提升至 83.9% |
| IE1 缺失（Hg） | 1 个元素 | 单独检查汞的维基 infobox 结构 |
| IE2~IE4_eV 全部为空（PubChem） | 118 个元素 | 扩展 PubChem 抓取逻辑抓取多级电离能 |
| MetallicRadius_pm 全部为空 | 118 个元素 | 针对性解析维基百科 "atomic radius: empirical" 字段 |

### P2 — 低优先级（扩展数据）

| 问题 | 影响范围 | 建议修复方案 |
|------|----------|-------------|
| Electronegativity_AllredRochow | 118 个元素 | 从 WebElements/NIST 补充 |
| E0_Acidic_V / E0_Basic_V | 118 个元素 | 从各元素"水溶液化学"段落或专门电化学表提取 |
| LatimerDiagram | 118 个元素 | 维基百科为图片，需 OCR 或文本重建 |
| IE4_kJmol（PP 表） | 118 个元素 | 维基百科通常只有 1~3 级；需从 NIST 获取 |
| Ar_Uncertainty（PubChem） | 118 个元素 | 扩展 PubChem 抓取字段 |

---

## 七、数据来源质量说明

### 已正确抓取的数据源

- **中文维基百科**：中文名称、基础物理属性（熔点/沸点格式较统一）
- **英文维基百科**：原子量（部分）、周期定位（Group/Period/Block）、电子构型、电离能（1~3级）、Pauling 电负性、共价半径、氧化态
- **PubChem API**：精确原子量（100%）、相态（100%）、密度（81.4%）、范德华半径（83.9%）、IE1（91.5%）、电子构型（99.2%）

### 抓取失败的常见原因

1. **字段名差异**：英式 "Ionisation" vs 美式 "Ionization"（已部分修复）
2. **Infobox 结构差异**：部分元素使用新版 infobox（`ib-element` 类），字段位置变化
3. **colspan 表头**：如 "Standard atomic weight" 占用整行，数据在下一行（已修复）
4. **数值格式多样**：密度有 g/cm³、g/L、"predicted"、括号注释等
5. **定义性缺失**：稀有气体无电负性；范德华半径对金属元素通常未定义
6. **超重元素**：Z≥104 的元素实验数据极少，大部分属性为理论预测值
7. **放射性元素**：Tc, Pm, Po, At, Rn, Fr, Ra 等无稳定同位素，IUPAC 不定义标准原子量

---

## 八、后续优化 checklist

### P0（立即修复）
- [ ] 从 Properties_PubChem 迁移 Density_gcm3 数据至 Elements_Master（可直接修复 98 个元素）
- [ ] 修复 Category 字段抓取（检查新版 infobox 字段名）或基于 Subcategory 推导
- [ ] 修复 UnpairedElectrons 推导正则，检查 ValenceElectrons 实际格式

### P1（近期处理）
- [ ] 利用 PubChem Ar_Exact（100%）回填 Elements_Master.Ar 放射性元素条目
- [ ] 从 Properties_PubChem 补充 VanderWaalsRadius_pm 至 Properties_Periodic（可从 46.6% 提升至约 83.9%）
- [ ] 修复 IE1 Hg 缺失（检查汞维基 infobox 结构）
- [ ] 扩展 PubChem 抓取脚本以获取 IE2/IE3/IE4（eV）
- [ ] 补充 MetallicRadius_pm 抓取逻辑

### P2（长期规划）
- [ ] 从 NIST 补充 IE4_kJmol、Allred-Rochow 电负性、EA 补充数据
- [ ] 设计从电化学专题页面抓取 E0 数据方案
- [ ] 评估是否引入 WebElements / ChemSpider 作为结构化数据补充源
- [ ] Ar_Uncertainty 字段抓取实现

---

*本报告基于 `periodic_table.db` 实际数据生成，用于指导后续数据质量优化工作。*
