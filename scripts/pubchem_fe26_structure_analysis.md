# PubChem pug_view/data/element/26 JSON 结构分析

> 分析对象：Fe（铁，Z=26）
> 数据来源：scripts/PubChem-26-Fe.json
> 生成目的：指导 build_db.py PubChem 解析逻辑优化

---

## 一、JSON 顶层结构

```
Record
├── RecordType: "AtomicNumber"
├── RecordNumber: 26
├── RecordTitle: "Iron"
└── Section[]
    ├── Identifiers       # 元素名、符号、InChI、InChIKey
    ├── Properties        # ★ 脚本主要解析目标
    ├── History
    ├── Description
    ├── Uses
    ├── Sources
    ├── Compounds
    └── Isotopes
```

---

## 二、Properties 子节点完整列表

每个子节点均为 `{ TOCHeading, Description, URL?, Information[] }` 结构。

| TOCHeading | 说明 | Value 典型格式 |
|------------|------|---------------|
| Atomic Weight | 原子量 | StringWithMarkup: "55.845(2)" |
| Electron Configuration | 电子排布 | StringWithMarkup: "[Ar]4s23d6" |
| Atomic Radius | 原子半径 | 含 Van der Waals / Empirical 等多种 |
| Oxidation States | 氧化态 | StringWithMarkup: "+3, +2" 或全氧化态列表 |
| Ground Level | 基态 | StringWithMarkup: "5D4" |
| Ionization Energy | 电离能 | StringWithMarkup: "7.902 eV" 或 "7.9024681 ± 0.0000012 eV" |
| Electronegativity | 电负性 | Number + Unit (Pauling Scale / Allen Scale) |
| Electron Affinity | 电子亲和能 | Number + Unit: "eV" |
| Atomic Spectra | 原子光谱 | StringWithMarkup |
| Physical Description | 物理描述 | StringWithMarkup: "Solid" |
| Element Classification | 元素分类 | StringWithMarkup: "Metal" |
| Element Period Number | 周期数 | String: "4" |
| Element Group Number | 族序 | String: "8" |
| Density | 密度 | StringWithMarkup: "7.874 grams per cubic centimeter" |
| Melting Point | 熔点 | StringWithMarkup: "1811 K (1538°C or 2800°F)" |
| Boiling Point | 沸点 | StringWithMarkup: "3134 K (2861°C or 5182°F)" |
| Estimated Crustal Abundance | 地壳丰度 | StringWithMarkup |
| Estimated Oceanic Abundance | 海洋丰度 | StringWithMarkup |

---

## 三、关键字段详细解析

### 3.1 Atomic Weight
```json
{
  "ReferenceNumber": 3,
  "Name": "Atomic Weight",
  "Value": {
    "StringWithMarkup": [{"String": "55.845(2)"}]
  }
}
```
**脚本处理**：✓ StringWithMarkup 格式能正确解析，`_parse_ar("55.845(2)")` 提取成功

---

### 3.2 Ionization Energy（电离能）
**重要：可能有多条 Information，记录不同来源的测量值**

```json
// 第一条（ReferenceNumber=5）
{
  "ReferenceNumber": 5,
  "Value": {
    "StringWithMarkup": [{"String": "7.902 eV"}]
  }
}

// 第二条（ReferenceNumber=7，含参考文献和更高精度值）
{
  "ReferenceNumber": 7,
  "Reference": ["High Excitation Rydberg Levels of Fe I..."],
  "Value": {
    "StringWithMarkup": [{"String": "7.9024681 ± 0.0000012 eV"}]
  }
}
```
**脚本处理**：✓ 取 `infos[0]`（第一条），正则 `r"([\d.]+)\s*eV"` 能匹配
**潜在问题**：不取第二条可能遗漏更高精度数据；且目前只取第一条

---

### 3.3 Electron Affinity（电子亲和能）
**重要：可能有多条 Information，来自不同文献，且数值不同**

```json
// 第一条
{
  "ReferenceNumber": 9,
  "Value": {
    "Number": [0.163],
    "Unit": "eV"
  }
}

// 第二条（不同文献）
{
  "ReferenceNumber": 9,
  "Value": {
    "Number": [0.46],
    "Unit": "eV"
  }
}
```
**脚本处理**：✓ 取 `infos[0]`，取 `Number[0]`，目前结果为 `0.163` eV
**注意**：两条数据不一致（0.163 vs 0.46），需确认哪个更权威或保留全部

---

### 3.4 Electronegativity（电负性）
**重要：可能有多条 Information，分别来自 Pauling Scale / Allen Scale 等不同标度**

```json
// Pauling Scale
{
  "ReferenceNumber": 9,
  "Name": "Pauling Scale Electronegativity",
  "Value": {
    "Number": [1.83],
    "Unit": "(Pauling Scale)"
  }
}

// Allen Scale
{
  "ReferenceNumber": 9,
  "Name": "Allen Scale Electronegativity",
  "Value": {
    "Number": [2.2],   // 示例，实际 Fe 的 Allen 值需查表
    "Unit": "(Allen Scale)"
  }
}
```
**脚本处理**：遍历 `infos`，将 `Number` 数组依次赋值给 EN_Pauling / EN_Mulliken
**问题**：实际 JSON 中第二条 `Number` 是 Allen Scale，不是 Mulliken，脚本逻辑假设第二条是 Mulliken 不正确

---

### 3.5 Atomic Radius（原子半径）
**重要：同一个 heading 下有多条 Information，类型不同**

```json
// Van der Waals 半径（StringWithMarkup 格式）
{
  "ReferenceNumber": 6,
  "Name": "Van der Waals Atomic Radius",
  "Value": {
    "StringWithMarkup": [{"String": "194 pm (Van der Waals)"}]
  }
}

// Empirical 半径（Number 格式，无 StringWithMarkup）
{
  "ReferenceNumber": 9,
  "Name": "Empirical Atomic Radius",
  "Value": {
    "Number": [140],
    "Unit": "pm (Empirical)"
  }
}
```
**脚本处理**：
- `CovalentRadius_pm`：在 StringWithMarkup 中查找含 "Covalent" 的条目 → Fe 无此条目，**漏抓**
- `VanderWaalsRadius_pm`：正则 `r"(\d+)\s*pm"` 匹配含 "Van der Waals" 的 StringWithMarkup → 提取到 `194`

**问题**：Empirical radius 有 `Number=140 pm` 但脚本未处理，且 Fe 实际共价半径约 140 pm（ Slater 半径），可考虑回退使用

---

### 3.6 Oxidation States（氧化态）
```json
// 常见氧化态
{
  "Value": {
    "StringWithMarkup": [{"String": "+3, +2"}]
  }
}

// 全部氧化态
{
  "Value": {
    "StringWithMarkup": [{"String": "-4, -2, -1, +1,+2, +3, +4, +5,+6, +7 (an amphoteric oxide)"}]
  }
}
```
**脚本处理**：✓ 取 `infos[0]`，目前取到 `"+3, +2"`

---

### 3.7 Density / Melting Point / Boiling Point
```json
// Density
{
  "Value": {
    "StringWithMarkup": [{"String": "7.874 grams per cubic centimeter"}]
  }
}

// Melting Point（两条数据）
{
  "Value": {
    "StringWithMarkup": [{"String": "1811 K (1538°C or 2800°F)"}]
  }
}
// 第二条
{
  "Value": {
    "StringWithMarkup": [{"String": "1538°C"}]
  }
}

// Boiling Point
{
  "Value": {
    "StringWithMarkup": [{"String": "3134 K (2861°C or 5182°F)"}]
  }
}
```
**脚本处理**：✓ 均通过 StringWithMarkup 正则提取

---

## 四、build_db.py 解析逻辑问题汇总

| # | 字段 | 问题描述 | 影响 |
|---|------|----------|------|
| 1 | Electronegativity | 假设 `infos[1].Number` 是 Mulliken，实际是 Allen Scale | 58.5% 数据可能误标 |
| 2 | Atomic Radius (Covalent) | 只查 StringWithMarkup 中含 "Covalent" 的条目，漏掉 Number 格式的 Empirical 数据 | 31 个元素 CovalentRadius 缺失 |
| 3 | Electron Affinity | 只取 `infos[0]`，不处理多条数据冲突或全部保留 | 43 个元素 EA 缺失（部分因只取第一条） |
| 4 | Ionization Energy | 只取第一条，不处理多条 | 无法获取更高精度值 |
| 5 | Oxidation States | 只取 `infos[0]`（常见氧化态），无法获取全部氧化态 | 1 个元素 OxidationStates 缺失（Og） |

---

## 五、建议优化方向

1. **Electronegativity**：根据 `Name` 字段（而非索引位置）判断是 Pauling 还是 Mulliken/Allen Scale
2. **Atomic Radius**：同时处理 `Number` 格式的 Empirical radius 作为 CovalentRadius 回退值
3. **Electron Affinity**：保留所有 EA 值或优先取有明确文献的那条
4. **Ionization Energy**：可考虑取最后一条（通常精度更高），或取全部级数
5. **Oxidation States**：根据 `Name` 区分"常见"与"全部"，按需取用

---

*本文件为 scripts/build_db.py PubChem 解析逻辑优化提供参考依据。*
