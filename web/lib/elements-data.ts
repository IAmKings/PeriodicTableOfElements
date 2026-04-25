export interface Element {
  atomicNumber: number
  symbol: string
  nameEn: string
  nameZh: string
  atomicMass: number
  category: ElementCategory
  electronConfiguration: string
  electronegativity: number | null
  meltingPoint: number | null
  boilingPoint: number | null
  density: number | null
  discoveryYear: number | null
  description: string
  group: number | null
  period: number
}

export type ElementCategory =
  | "alkali-metal"
  | "alkaline-earth-metal"
  | "transition-metal"
  | "post-transition-metal"
  | "metalloid"
  | "nonmetal"
  | "halogen"
  | "noble-gas"
  | "lanthanide"
  | "actinide"

export const categoryInfo: Record<ElementCategory, { nameZh: string; nameEn: string; color: string; glow: string }> = {
  "alkali-metal": { nameZh: "碱金属", nameEn: "Alkali Metal", color: "bg-red-500/80", glow: "shadow-red-500/50" },
  "alkaline-earth-metal": { nameZh: "碱土金属", nameEn: "Alkaline Earth", color: "bg-orange-500/80", glow: "shadow-orange-500/50" },
  "transition-metal": { nameZh: "过渡金属", nameEn: "Transition Metal", color: "bg-yellow-500/80", glow: "shadow-yellow-500/50" },
  "post-transition-metal": { nameZh: "后过渡金属", nameEn: "Post-transition", color: "bg-green-500/80", glow: "shadow-green-500/50" },
  "metalloid": { nameZh: "准金属", nameEn: "Metalloid", color: "bg-teal-500/80", glow: "shadow-teal-500/50" },
  "nonmetal": { nameZh: "非金属", nameEn: "Nonmetal", color: "bg-cyan-500/80", glow: "shadow-cyan-500/50" },
  "halogen": { nameZh: "卤素", nameEn: "Halogen", color: "bg-blue-500/80", glow: "shadow-blue-500/50" },
  "noble-gas": { nameZh: "惰性气体", nameEn: "Noble Gas", color: "bg-indigo-500/80", glow: "shadow-indigo-500/50" },
  "lanthanide": { nameZh: "镧系元素", nameEn: "Lanthanide", color: "bg-pink-500/80", glow: "shadow-pink-500/50" },
  "actinide": { nameZh: "锕系元素", nameEn: "Actinide", color: "bg-rose-500/80", glow: "shadow-rose-500/50" },
}

export const elements: Element[] = [
  // Period 1
  { atomicNumber: 1, symbol: "H", nameEn: "Hydrogen", nameZh: "氢", atomicMass: 1.008, category: "nonmetal", electronConfiguration: "1s¹", electronegativity: 2.2, meltingPoint: -259.16, boilingPoint: -252.87, density: 0.00008988, discoveryYear: 1766, description: "宇宙中最轻、最丰富的元素，是恒星和太阳的主要燃料。", group: 1, period: 1 },
  { atomicNumber: 2, symbol: "He", nameEn: "Helium", nameZh: "氦", atomicMass: 4.003, category: "noble-gas", electronConfiguration: "1s²", electronegativity: null, meltingPoint: -272.2, boilingPoint: -268.93, density: 0.0001785, discoveryYear: 1868, description: "第二轻的元素，常用于气球和低温科学研究。", group: 18, period: 1 },
  
  // Period 2
  { atomicNumber: 3, symbol: "Li", nameEn: "Lithium", nameZh: "锂", atomicMass: 6.94, category: "alkali-metal", electronConfiguration: "[He] 2s¹", electronegativity: 0.98, meltingPoint: 180.54, boilingPoint: 1342, density: 0.534, discoveryYear: 1817, description: "最轻的金属元素，广泛用于电池和药物。", group: 1, period: 2 },
  { atomicNumber: 4, symbol: "Be", nameEn: "Beryllium", nameZh: "铍", atomicMass: 9.012, category: "alkaline-earth-metal", electronConfiguration: "[He] 2s²", electronegativity: 1.57, meltingPoint: 1287, boilingPoint: 2469, density: 1.85, discoveryYear: 1798, description: "轻质高强度金属，用于航空航天和X射线窗口。", group: 2, period: 2 },
  { atomicNumber: 5, symbol: "B", nameEn: "Boron", nameZh: "硼", atomicMass: 10.81, category: "metalloid", electronConfiguration: "[He] 2s² 2p¹", electronegativity: 2.04, meltingPoint: 2076, boilingPoint: 3927, density: 2.34, discoveryYear: 1808, description: "准金属元素，用于玻璃纤维和高强度材料。", group: 13, period: 2 },
  { atomicNumber: 6, symbol: "C", nameEn: "Carbon", nameZh: "碳", atomicMass: 12.011, category: "nonmetal", electronConfiguration: "[He] 2s² 2p²", electronegativity: 2.55, meltingPoint: 3550, boilingPoint: 4027, density: 2.267, discoveryYear: null, description: "生命的基础元素，可形成钻石、石墨等多种同素异形体。", group: 14, period: 2 },
  { atomicNumber: 7, symbol: "N", nameEn: "Nitrogen", nameZh: "氮", atomicMass: 14.007, category: "nonmetal", electronConfiguration: "[He] 2s² 2p³", electronegativity: 3.04, meltingPoint: -210.1, boilingPoint: -195.79, density: 0.0012506, discoveryYear: 1772, description: "大气中含量最多的气体，是蛋白质的重要组成元素。", group: 15, period: 2 },
  { atomicNumber: 8, symbol: "O", nameEn: "Oxygen", nameZh: "氧", atomicMass: 15.999, category: "nonmetal", electronConfiguration: "[He] 2s² 2p⁴", electronegativity: 3.44, meltingPoint: -218.79, boilingPoint: -182.95, density: 0.001429, discoveryYear: 1774, description: "生命必需的气体，支持燃烧和呼吸作用。", group: 16, period: 2 },
  { atomicNumber: 9, symbol: "F", nameEn: "Fluorine", nameZh: "氟", atomicMass: 18.998, category: "halogen", electronConfiguration: "[He] 2s² 2p⁵", electronegativity: 3.98, meltingPoint: -219.62, boilingPoint: -188.12, density: 0.001696, discoveryYear: 1886, description: "最活泼的非金属元素，用于牙膏和制冷剂。", group: 17, period: 2 },
  { atomicNumber: 10, symbol: "Ne", nameEn: "Neon", nameZh: "氖", atomicMass: 20.18, category: "noble-gas", electronConfiguration: "[He] 2s² 2p⁶", electronegativity: null, meltingPoint: -248.59, boilingPoint: -246.08, density: 0.0008999, discoveryYear: 1898, description: "惰性气体，常用于霓虹灯和激光器。", group: 18, period: 2 },
  
  // Period 3
  { atomicNumber: 11, symbol: "Na", nameEn: "Sodium", nameZh: "钠", atomicMass: 22.99, category: "alkali-metal", electronConfiguration: "[Ne] 3s¹", electronegativity: 0.93, meltingPoint: 97.72, boilingPoint: 883, density: 0.971, discoveryYear: 1807, description: "高活性金属，食盐的主要成分。", group: 1, period: 3 },
  { atomicNumber: 12, symbol: "Mg", nameEn: "Magnesium", nameZh: "镁", atomicMass: 24.305, category: "alkaline-earth-metal", electronConfiguration: "[Ne] 3s²", electronegativity: 1.31, meltingPoint: 650, boilingPoint: 1090, density: 1.738, discoveryYear: 1755, description: "轻质金属，用于合金和烟火。", group: 2, period: 3 },
  { atomicNumber: 13, symbol: "Al", nameEn: "Aluminum", nameZh: "铝", atomicMass: 26.982, category: "post-transition-metal", electronConfiguration: "[Ne] 3s² 3p¹", electronegativity: 1.61, meltingPoint: 660.32, boilingPoint: 2519, density: 2.698, discoveryYear: 1825, description: "地壳中含量最多的金属，广泛用于建筑和包装。", group: 13, period: 3 },
  { atomicNumber: 14, symbol: "Si", nameEn: "Silicon", nameZh: "硅", atomicMass: 28.085, category: "metalloid", electronConfiguration: "[Ne] 3s² 3p²", electronegativity: 1.9, meltingPoint: 1414, boilingPoint: 3265, density: 2.329, discoveryYear: 1824, description: "半导体工业的基础材料，用于芯片制造。", group: 14, period: 3 },
  { atomicNumber: 15, symbol: "P", nameEn: "Phosphorus", nameZh: "磷", atomicMass: 30.974, category: "nonmetal", electronConfiguration: "[Ne] 3s² 3p³", electronegativity: 2.19, meltingPoint: 44.15, boilingPoint: 280.5, density: 1.82, discoveryYear: 1669, description: "生命必需元素，存在于DNA和骨骼中。", group: 15, period: 3 },
  { atomicNumber: 16, symbol: "S", nameEn: "Sulfur", nameZh: "硫", atomicMass: 32.06, category: "nonmetal", electronConfiguration: "[Ne] 3s² 3p⁴", electronegativity: 2.58, meltingPoint: 115.21, boilingPoint: 444.6, density: 2.067, discoveryYear: null, description: "黄色固体，用于硫酸生产和橡胶硫化。", group: 16, period: 3 },
  { atomicNumber: 17, symbol: "Cl", nameEn: "Chlorine", nameZh: "氯", atomicMass: 35.45, category: "halogen", electronConfiguration: "[Ne] 3s² 3p⁵", electronegativity: 3.16, meltingPoint: -101.5, boilingPoint: -34.04, density: 0.003214, discoveryYear: 1774, description: "黄绿色有毒气体，用于消毒和制造塑料。", group: 17, period: 3 },
  { atomicNumber: 18, symbol: "Ar", nameEn: "Argon", nameZh: "氩", atomicMass: 39.948, category: "noble-gas", electronConfiguration: "[Ne] 3s² 3p⁶", electronegativity: null, meltingPoint: -189.35, boilingPoint: -185.85, density: 0.0017837, discoveryYear: 1894, description: "大气中第三丰富的气体，用于焊接保护。", group: 18, period: 3 },
  
  // Period 4
  { atomicNumber: 19, symbol: "K", nameEn: "Potassium", nameZh: "钾", atomicMass: 39.098, category: "alkali-metal", electronConfiguration: "[Ar] 4s¹", electronegativity: 0.82, meltingPoint: 63.38, boilingPoint: 759, density: 0.862, discoveryYear: 1807, description: "对植物生长至关重要的元素。", group: 1, period: 4 },
  { atomicNumber: 20, symbol: "Ca", nameEn: "Calcium", nameZh: "钙", atomicMass: 40.078, category: "alkaline-earth-metal", electronConfiguration: "[Ar] 4s²", electronegativity: 1.0, meltingPoint: 842, boilingPoint: 1484, density: 1.54, discoveryYear: 1808, description: "骨骼和牙齿的主要成分。", group: 2, period: 4 },
  { atomicNumber: 21, symbol: "Sc", nameEn: "Scandium", nameZh: "钪", atomicMass: 44.956, category: "transition-metal", electronConfiguration: "[Ar] 3d¹ 4s²", electronegativity: 1.36, meltingPoint: 1541, boilingPoint: 2836, density: 2.989, discoveryYear: 1879, description: "用于高强度铝合金。", group: 3, period: 4 },
  { atomicNumber: 22, symbol: "Ti", nameEn: "Titanium", nameZh: "钛", atomicMass: 47.867, category: "transition-metal", electronConfiguration: "[Ar] 3d² 4s²", electronegativity: 1.54, meltingPoint: 1668, boilingPoint: 3287, density: 4.54, discoveryYear: 1791, description: "轻质高强度金属，用于航空和医疗植入物。", group: 4, period: 4 },
  { atomicNumber: 23, symbol: "V", nameEn: "Vanadium", nameZh: "钒", atomicMass: 50.942, category: "transition-metal", electronConfiguration: "[Ar] 3d³ 4s²", electronegativity: 1.63, meltingPoint: 1910, boilingPoint: 3407, density: 6.11, discoveryYear: 1801, description: "用于钢铁合金和储能电池。", group: 5, period: 4 },
  { atomicNumber: 24, symbol: "Cr", nameEn: "Chromium", nameZh: "铬", atomicMass: 51.996, category: "transition-metal", electronConfiguration: "[Ar] 3d⁵ 4s¹", electronegativity: 1.66, meltingPoint: 1907, boilingPoint: 2671, density: 7.15, discoveryYear: 1798, description: "用于不锈钢和电镀。", group: 6, period: 4 },
  { atomicNumber: 25, symbol: "Mn", nameEn: "Manganese", nameZh: "锰", atomicMass: 54.938, category: "transition-metal", electronConfiguration: "[Ar] 3d⁵ 4s²", electronegativity: 1.55, meltingPoint: 1246, boilingPoint: 2061, density: 7.44, discoveryYear: 1774, description: "用于钢铁生产和电池。", group: 7, period: 4 },
  { atomicNumber: 26, symbol: "Fe", nameEn: "Iron", nameZh: "铁", atomicMass: 55.845, category: "transition-metal", electronConfiguration: "[Ar] 3d⁶ 4s²", electronegativity: 1.83, meltingPoint: 1538, boilingPoint: 2861, density: 7.874, discoveryYear: null, description: "地球上最常用的金属，是钢铁的主要成分。", group: 8, period: 4 },
  { atomicNumber: 27, symbol: "Co", nameEn: "Cobalt", nameZh: "钴", atomicMass: 58.933, category: "transition-metal", electronConfiguration: "[Ar] 3d⁷ 4s²", electronegativity: 1.88, meltingPoint: 1495, boilingPoint: 2927, density: 8.86, discoveryYear: 1735, description: "用于锂电池和蓝色颜料。", group: 9, period: 4 },
  { atomicNumber: 28, symbol: "Ni", nameEn: "Nickel", nameZh: "镍", atomicMass: 58.693, category: "transition-metal", electronConfiguration: "[Ar] 3d⁸ 4s²", electronegativity: 1.91, meltingPoint: 1455, boilingPoint: 2913, density: 8.912, discoveryYear: 1751, description: "用于不锈钢和硬币。", group: 10, period: 4 },
  { atomicNumber: 29, symbol: "Cu", nameEn: "Copper", nameZh: "铜", atomicMass: 63.546, category: "transition-metal", electronConfiguration: "[Ar] 3d¹⁰ 4s¹", electronegativity: 1.9, meltingPoint: 1084.62, boilingPoint: 2562, density: 8.96, discoveryYear: null, description: "优良的导电体，广泛用于电线和管道。", group: 11, period: 4 },
  { atomicNumber: 30, symbol: "Zn", nameEn: "Zinc", nameZh: "锌", atomicMass: 65.38, category: "transition-metal", electronConfiguration: "[Ar] 3d¹⁰ 4s²", electronegativity: 1.65, meltingPoint: 419.53, boilingPoint: 907, density: 7.134, discoveryYear: null, description: "用于镀锌防腐和电池。", group: 12, period: 4 },
  { atomicNumber: 31, symbol: "Ga", nameEn: "Gallium", nameZh: "镓", atomicMass: 69.723, category: "post-transition-metal", electronConfiguration: "[Ar] 3d¹⁰ 4s² 4p¹", electronegativity: 1.81, meltingPoint: 29.76, boilingPoint: 2204, density: 5.907, discoveryYear: 1875, description: "低熔点金属，用于半导体和LED。", group: 13, period: 4 },
  { atomicNumber: 32, symbol: "Ge", nameEn: "Germanium", nameZh: "锗", atomicMass: 72.63, category: "metalloid", electronConfiguration: "[Ar] 3d¹⁰ 4s² 4p²", electronegativity: 2.01, meltingPoint: 938.25, boilingPoint: 2833, density: 5.323, discoveryYear: 1886, description: "半导体材料，用于光纤和红外光学。", group: 14, period: 4 },
  { atomicNumber: 33, symbol: "As", nameEn: "Arsenic", nameZh: "砷", atomicMass: 74.922, category: "metalloid", electronConfiguration: "[Ar] 3d¹⁰ 4s² 4p³", electronegativity: 2.18, meltingPoint: 817, boilingPoint: 614, density: 5.776, discoveryYear: null, description: "准金属，历史上曾被用作毒药。", group: 15, period: 4 },
  { atomicNumber: 34, symbol: "Se", nameEn: "Selenium", nameZh: "硒", atomicMass: 78.971, category: "nonmetal", electronConfiguration: "[Ar] 3d¹⁰ 4s² 4p⁴", electronegativity: 2.55, meltingPoint: 221, boilingPoint: 685, density: 4.809, discoveryYear: 1817, description: "人体必需的微量元素，用于电子设备。", group: 16, period: 4 },
  { atomicNumber: 35, symbol: "Br", nameEn: "Bromine", nameZh: "溴", atomicMass: 79.904, category: "halogen", electronConfiguration: "[Ar] 3d¹⁰ 4s² 4p⁵", electronegativity: 2.96, meltingPoint: -7.2, boilingPoint: 58.8, density: 3.122, discoveryYear: 1826, description: "室温下唯一呈液态的非金属元素。", group: 17, period: 4 },
  { atomicNumber: 36, symbol: "Kr", nameEn: "Krypton", nameZh: "氪", atomicMass: 83.798, category: "noble-gas", electronConfiguration: "[Ar] 3d¹⁰ 4s² 4p⁶", electronegativity: 3.0, meltingPoint: -157.36, boilingPoint: -153.22, density: 0.003733, discoveryYear: 1898, description: "稀有气体，用于照明和激光。", group: 18, period: 4 },
  
  // Period 5
  { atomicNumber: 37, symbol: "Rb", nameEn: "Rubidium", nameZh: "铷", atomicMass: 85.468, category: "alkali-metal", electronConfiguration: "[Kr] 5s¹", electronegativity: 0.82, meltingPoint: 39.31, boilingPoint: 688, density: 1.532, discoveryYear: 1861, description: "高活性碱金属，用于原子钟。", group: 1, period: 5 },
  { atomicNumber: 38, symbol: "Sr", nameEn: "Strontium", nameZh: "锶", atomicMass: 87.62, category: "alkaline-earth-metal", electronConfiguration: "[Kr] 5s²", electronegativity: 0.95, meltingPoint: 777, boilingPoint: 1382, density: 2.64, discoveryYear: 1790, description: "用于烟火和CRT显示器。", group: 2, period: 5 },
  { atomicNumber: 39, symbol: "Y", nameEn: "Yttrium", nameZh: "钇", atomicMass: 88.906, category: "transition-metal", electronConfiguration: "[Kr] 4d¹ 5s²", electronegativity: 1.22, meltingPoint: 1522, boilingPoint: 3345, density: 4.469, discoveryYear: 1794, description: "用于LED和超导体。", group: 3, period: 5 },
  { atomicNumber: 40, symbol: "Zr", nameEn: "Zirconium", nameZh: "锆", atomicMass: 91.224, category: "transition-metal", electronConfiguration: "[Kr] 4d² 5s²", electronegativity: 1.33, meltingPoint: 1855, boilingPoint: 4409, density: 6.506, discoveryYear: 1789, description: "耐腐蚀金属，用于核反应堆。", group: 4, period: 5 },
  { atomicNumber: 41, symbol: "Nb", nameEn: "Niobium", nameZh: "铌", atomicMass: 92.906, category: "transition-metal", electronConfiguration: "[Kr] 4d⁴ 5s¹", electronegativity: 1.6, meltingPoint: 2477, boilingPoint: 4744, density: 8.57, discoveryYear: 1801, description: "用于超导磁铁和钢合金。", group: 5, period: 5 },
  { atomicNumber: 42, symbol: "Mo", nameEn: "Molybdenum", nameZh: "钼", atomicMass: 95.95, category: "transition-metal", electronConfiguration: "[Kr] 4d⁵ 5s¹", electronegativity: 2.16, meltingPoint: 2623, boilingPoint: 4639, density: 10.22, discoveryYear: 1781, description: "高熔点金属，用于钢铁合金。", group: 6, period: 5 },
  { atomicNumber: 43, symbol: "Tc", nameEn: "Technetium", nameZh: "锝", atomicMass: 98, category: "transition-metal", electronConfiguration: "[Kr] 4d⁵ 5s²", electronegativity: 1.9, meltingPoint: 2157, boilingPoint: 4265, density: 11.5, discoveryYear: 1937, description: "第一个人工合成的元素，用于医学成像。", group: 7, period: 5 },
  { atomicNumber: 44, symbol: "Ru", nameEn: "Ruthenium", nameZh: "钌", atomicMass: 101.07, category: "transition-metal", electronConfiguration: "[Kr] 4d⁷ 5s¹", electronegativity: 2.2, meltingPoint: 2334, boilingPoint: 4150, density: 12.37, discoveryYear: 1844, description: "铂族金属，用于催化剂和电子设备。", group: 8, period: 5 },
  { atomicNumber: 45, symbol: "Rh", nameEn: "Rhodium", nameZh: "铑", atomicMass: 102.91, category: "transition-metal", electronConfiguration: "[Kr] 4d⁸ 5s¹", electronegativity: 2.28, meltingPoint: 1964, boilingPoint: 3695, density: 12.41, discoveryYear: 1803, description: "最贵重的金属之一，用于催化转化器。", group: 9, period: 5 },
  { atomicNumber: 46, symbol: "Pd", nameEn: "Palladium", nameZh: "钯", atomicMass: 106.42, category: "transition-metal", electronConfiguration: "[Kr] 4d¹⁰", electronegativity: 2.2, meltingPoint: 1554.9, boilingPoint: 2963, density: 12.02, discoveryYear: 1803, description: "用于汽车催化转化器和电子设备。", group: 10, period: 5 },
  { atomicNumber: 47, symbol: "Ag", nameEn: "Silver", nameZh: "银", atomicMass: 107.87, category: "transition-metal", electronConfiguration: "[Kr] 4d¹⁰ 5s¹", electronegativity: 1.93, meltingPoint: 961.78, boilingPoint: 2162, density: 10.501, discoveryYear: null, description: "导电性最好的金属，用于珠宝和电子。", group: 11, period: 5 },
  { atomicNumber: 48, symbol: "Cd", nameEn: "Cadmium", nameZh: "镉", atomicMass: 112.41, category: "transition-metal", electronConfiguration: "[Kr] 4d¹⁰ 5s²", electronegativity: 1.69, meltingPoint: 321.07, boilingPoint: 767, density: 8.69, discoveryYear: 1817, description: "有毒金属，曾用于电池和颜料。", group: 12, period: 5 },
  { atomicNumber: 49, symbol: "In", nameEn: "Indium", nameZh: "铟", atomicMass: 114.82, category: "post-transition-metal", electronConfiguration: "[Kr] 4d¹⁰ 5s² 5p¹", electronegativity: 1.78, meltingPoint: 156.6, boilingPoint: 2072, density: 7.31, discoveryYear: 1863, description: "软金属，用于触摸屏和焊料。", group: 13, period: 5 },
  { atomicNumber: 50, symbol: "Sn", nameEn: "Tin", nameZh: "锡", atomicMass: 118.71, category: "post-transition-metal", electronConfiguration: "[Kr] 4d¹⁰ 5s² 5p²", electronegativity: 1.96, meltingPoint: 231.93, boilingPoint: 2602, density: 7.287, discoveryYear: null, description: "用于焊料和食品包装。", group: 14, period: 5 },
  { atomicNumber: 51, symbol: "Sb", nameEn: "Antimony", nameZh: "锑", atomicMass: 121.76, category: "metalloid", electronConfiguration: "[Kr] 4d¹⁰ 5s² 5p³", electronegativity: 2.05, meltingPoint: 630.63, boilingPoint: 1587, density: 6.685, discoveryYear: null, description: "用于阻燃剂和合金。", group: 15, period: 5 },
  { atomicNumber: 52, symbol: "Te", nameEn: "Tellurium", nameZh: "碲", atomicMass: 127.6, category: "metalloid", electronConfiguration: "[Kr] 4d¹⁰ 5s² 5p⁴", electronegativity: 2.1, meltingPoint: 449.51, boilingPoint: 988, density: 6.232, discoveryYear: 1783, description: "用于太阳能电池和合金。", group: 16, period: 5 },
  { atomicNumber: 53, symbol: "I", nameEn: "Iodine", nameZh: "碘", atomicMass: 126.9, category: "halogen", electronConfiguration: "[Kr] 4d¹⁰ 5s² 5p⁵", electronegativity: 2.66, meltingPoint: 113.7, boilingPoint: 184.3, density: 4.93, discoveryYear: 1811, description: "人体必需的微量元素，用于甲状腺激素。", group: 17, period: 5 },
  { atomicNumber: 54, symbol: "Xe", nameEn: "Xenon", nameZh: "氙", atomicMass: 131.29, category: "noble-gas", electronConfiguration: "[Kr] 4d¹⁰ 5s² 5p⁶", electronegativity: 2.6, meltingPoint: -111.75, boilingPoint: -108.04, density: 0.005887, discoveryYear: 1898, description: "稀有气体，用于闪光灯和麻醉。", group: 18, period: 5 },
  
  // Period 6
  { atomicNumber: 55, symbol: "Cs", nameEn: "Cesium", nameZh: "铯", atomicMass: 132.91, category: "alkali-metal", electronConfiguration: "[Xe] 6s¹", electronegativity: 0.79, meltingPoint: 28.44, boilingPoint: 671, density: 1.873, discoveryYear: 1860, description: "用于原子钟的高精度计时。", group: 1, period: 6 },
  { atomicNumber: 56, symbol: "Ba", nameEn: "Barium", nameZh: "钡", atomicMass: 137.33, category: "alkaline-earth-metal", electronConfiguration: "[Xe] 6s²", electronegativity: 0.89, meltingPoint: 727, boilingPoint: 1845, density: 3.594, discoveryYear: 1808, description: "用于烟火和医学成像。", group: 2, period: 6 },
  // Lanthanides (57-71)
  { atomicNumber: 57, symbol: "La", nameEn: "Lanthanum", nameZh: "镧", atomicMass: 138.91, category: "lanthanide", electronConfiguration: "[Xe] 5d¹ 6s²", electronegativity: 1.1, meltingPoint: 920, boilingPoint: 3464, density: 6.145, discoveryYear: 1839, description: "镧系元素之首，用于催化剂和光学玻璃。", group: null, period: 6 },
  { atomicNumber: 58, symbol: "Ce", nameEn: "Cerium", nameZh: "铈", atomicMass: 140.12, category: "lanthanide", electronConfiguration: "[Xe] 4f¹ 5d¹ 6s²", electronegativity: 1.12, meltingPoint: 799, boilingPoint: 3443, density: 6.77, discoveryYear: 1803, description: "最丰富的稀土元素，用于抛光剂。", group: null, period: 6 },
  { atomicNumber: 59, symbol: "Pr", nameEn: "Praseodymium", nameZh: "镨", atomicMass: 140.91, category: "lanthanide", electronConfiguration: "[Xe] 4f³ 6s²", electronegativity: 1.13, meltingPoint: 931, boilingPoint: 3520, density: 6.773, discoveryYear: 1885, description: "用于强力磁铁和玻璃着色。", group: null, period: 6 },
  { atomicNumber: 60, symbol: "Nd", nameEn: "Neodymium", nameZh: "钕", atomicMass: 144.24, category: "lanthanide", electronConfiguration: "[Xe] 4f⁴ 6s²", electronegativity: 1.14, meltingPoint: 1016, boilingPoint: 3074, density: 7.007, discoveryYear: 1885, description: "用于世界上最强的永磁体。", group: null, period: 6 },
  { atomicNumber: 61, symbol: "Pm", nameEn: "Promethium", nameZh: "钷", atomicMass: 145, category: "lanthanide", electronConfiguration: "[Xe] 4f⁵ 6s²", electronegativity: 1.13, meltingPoint: 1042, boilingPoint: 3000, density: 7.26, discoveryYear: 1945, description: "放射性元素，用于核电池。", group: null, period: 6 },
  { atomicNumber: 62, symbol: "Sm", nameEn: "Samarium", nameZh: "钐", atomicMass: 150.36, category: "lanthanide", electronConfiguration: "[Xe] 4f⁶ 6s²", electronegativity: 1.17, meltingPoint: 1072, boilingPoint: 1794, density: 7.52, discoveryYear: 1879, description: "用于磁铁和癌症治疗。", group: null, period: 6 },
  { atomicNumber: 63, symbol: "Eu", nameEn: "Europium", nameZh: "铕", atomicMass: 151.96, category: "lanthanide", electronConfiguration: "[Xe] 4f⁷ 6s²", electronegativity: 1.2, meltingPoint: 822, boilingPoint: 1529, density: 5.243, discoveryYear: 1901, description: "用于荧光灯和防伪标记。", group: null, period: 6 },
  { atomicNumber: 64, symbol: "Gd", nameEn: "Gadolinium", nameZh: "钆", atomicMass: 157.25, category: "lanthanide", electronConfiguration: "[Xe] 4f⁷ 5d¹ 6s²", electronegativity: 1.2, meltingPoint: 1313, boilingPoint: 3273, density: 7.895, discoveryYear: 1880, description: "用于MRI造影剂和核反应堆。", group: null, period: 6 },
  { atomicNumber: 65, symbol: "Tb", nameEn: "Terbium", nameZh: "铽", atomicMass: 158.93, category: "lanthanide", electronConfiguration: "[Xe] 4f⁹ 6s²", electronegativity: 1.2, meltingPoint: 1356, boilingPoint: 3230, density: 8.229, discoveryYear: 1843, description: "用于绿色荧光粉和固态存储。", group: null, period: 6 },
  { atomicNumber: 66, symbol: "Dy", nameEn: "Dysprosium", nameZh: "镝", atomicMass: 162.5, category: "lanthanide", electronConfiguration: "[Xe] 4f¹⁰ 6s²", electronegativity: 1.22, meltingPoint: 1412, boilingPoint: 2567, density: 8.55, discoveryYear: 1886, description: "用于强力磁铁和激光材料。", group: null, period: 6 },
  { atomicNumber: 67, symbol: "Ho", nameEn: "Holmium", nameZh: "钬", atomicMass: 164.93, category: "lanthanide", electronConfiguration: "[Xe] 4f¹¹ 6s²", electronegativity: 1.23, meltingPoint: 1474, boilingPoint: 2700, density: 8.795, discoveryYear: 1878, description: "拥有最强磁矩的元素之一。", group: null, period: 6 },
  { atomicNumber: 68, symbol: "Er", nameEn: "Erbium", nameZh: "铒", atomicMass: 167.26, category: "lanthanide", electronConfiguration: "[Xe] 4f¹² 6s²", electronegativity: 1.24, meltingPoint: 1529, boilingPoint: 2868, density: 9.066, discoveryYear: 1843, description: "用于光纤放大器和激光。", group: null, period: 6 },
  { atomicNumber: 69, symbol: "Tm", nameEn: "Thulium", nameZh: "铥", atomicMass: 168.93, category: "lanthanide", electronConfiguration: "[Xe] 4f¹³ 6s²", electronegativity: 1.25, meltingPoint: 1545, boilingPoint: 1950, density: 9.321, discoveryYear: 1879, description: "最稀有的镧系元素之一。", group: null, period: 6 },
  { atomicNumber: 70, symbol: "Yb", nameEn: "Ytterbium", nameZh: "镱", atomicMass: 173.05, category: "lanthanide", electronConfiguration: "[Xe] 4f¹⁴ 6s²", electronegativity: 1.1, meltingPoint: 819, boilingPoint: 1196, density: 6.965, discoveryYear: 1878, description: "用于激光和医疗设备。", group: null, period: 6 },
  { atomicNumber: 71, symbol: "Lu", nameEn: "Lutetium", nameZh: "镥", atomicMass: 174.97, category: "lanthanide", electronConfiguration: "[Xe] 4f¹⁴ 5d¹ 6s²", electronegativity: 1.27, meltingPoint: 1663, boilingPoint: 3402, density: 9.84, discoveryYear: 1907, description: "镧系元素中最重的，用于PET扫描。", group: null, period: 6 },
  { atomicNumber: 72, symbol: "Hf", nameEn: "Hafnium", nameZh: "铪", atomicMass: 178.49, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d² 6s²", electronegativity: 1.3, meltingPoint: 2233, boilingPoint: 4603, density: 13.31, discoveryYear: 1923, description: "用于核反应堆控制棒和微处理器。", group: 4, period: 6 },
  { atomicNumber: 73, symbol: "Ta", nameEn: "Tantalum", nameZh: "钽", atomicMass: 180.95, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d³ 6s²", electronegativity: 1.5, meltingPoint: 3017, boilingPoint: 5458, density: 16.654, discoveryYear: 1802, description: "用于电子电容器和外科植入物。", group: 5, period: 6 },
  { atomicNumber: 74, symbol: "W", nameEn: "Tungsten", nameZh: "钨", atomicMass: 183.84, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d⁴ 6s²", electronegativity: 2.36, meltingPoint: 3422, boilingPoint: 5555, density: 19.25, discoveryYear: 1783, description: "熔点最高的金属，用于灯丝和切削工具。", group: 6, period: 6 },
  { atomicNumber: 75, symbol: "Re", nameEn: "Rhenium", nameZh: "铼", atomicMass: 186.21, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d⁵ 6s²", electronegativity: 1.9, meltingPoint: 3186, boilingPoint: 5596, density: 21.02, discoveryYear: 1925, description: "用于喷气发动机和催化剂。", group: 7, period: 6 },
  { atomicNumber: 76, symbol: "Os", nameEn: "Osmium", nameZh: "锇", atomicMass: 190.23, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d⁶ 6s²", electronegativity: 2.2, meltingPoint: 3033, boilingPoint: 5012, density: 22.59, discoveryYear: 1803, description: "密度最大的天然元素。", group: 8, period: 6 },
  { atomicNumber: 77, symbol: "Ir", nameEn: "Iridium", nameZh: "铱", atomicMass: 192.22, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d⁷ 6s²", electronegativity: 2.2, meltingPoint: 2446, boilingPoint: 4428, density: 22.56, discoveryYear: 1803, description: "最耐腐蚀的金属，用于火花塞。", group: 9, period: 6 },
  { atomicNumber: 78, symbol: "Pt", nameEn: "Platinum", nameZh: "铂", atomicMass: 195.08, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d⁹ 6s¹", electronegativity: 2.28, meltingPoint: 1768.3, boilingPoint: 3825, density: 21.46, discoveryYear: 1735, description: "贵金属，用于珠宝和催化转化器。", group: 10, period: 6 },
  { atomicNumber: 79, symbol: "Au", nameEn: "Gold", nameZh: "金", atomicMass: 196.97, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s¹", electronegativity: 2.54, meltingPoint: 1064.18, boilingPoint: 2856, density: 19.282, discoveryYear: null, description: "最具价值的贵金属，用于珠宝和货币。", group: 11, period: 6 },
  { atomicNumber: 80, symbol: "Hg", nameEn: "Mercury", nameZh: "汞", atomicMass: 200.59, category: "transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s²", electronegativity: 2.0, meltingPoint: -38.83, boilingPoint: 356.73, density: 13.5336, discoveryYear: null, description: "室温下唯一呈液态的金属。", group: 12, period: 6 },
  { atomicNumber: 81, symbol: "Tl", nameEn: "Thallium", nameZh: "铊", atomicMass: 204.38, category: "post-transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p¹", electronegativity: 1.62, meltingPoint: 304, boilingPoint: 1473, density: 11.85, discoveryYear: 1861, description: "有毒金属，用于电子设备和医学成像。", group: 13, period: 6 },
  { atomicNumber: 82, symbol: "Pb", nameEn: "Lead", nameZh: "铅", atomicMass: 207.2, category: "post-transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p²", electronegativity: 2.33, meltingPoint: 327.46, boilingPoint: 1749, density: 11.342, discoveryYear: null, description: "软重金属，历史上用于管道和电池。", group: 14, period: 6 },
  { atomicNumber: 83, symbol: "Bi", nameEn: "Bismuth", nameZh: "铋", atomicMass: 208.98, category: "post-transition-metal", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p³", electronegativity: 2.02, meltingPoint: 271.3, boilingPoint: 1564, density: 9.807, discoveryYear: 1753, description: "低毒性金属，用于药物和合金。", group: 15, period: 6 },
  { atomicNumber: 84, symbol: "Po", nameEn: "Polonium", nameZh: "钋", atomicMass: 209, category: "metalloid", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p⁴", electronegativity: 2.0, meltingPoint: 254, boilingPoint: 962, density: 9.32, discoveryYear: 1898, description: "高放射性元素，由居里夫人发现。", group: 16, period: 6 },
  { atomicNumber: 85, symbol: "At", nameEn: "Astatine", nameZh: "砹", atomicMass: 210, category: "halogen", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p⁵", electronegativity: 2.2, meltingPoint: 302, boilingPoint: 337, density: 7, discoveryYear: 1940, description: "最稀有的天然元素之一。", group: 17, period: 6 },
  { atomicNumber: 86, symbol: "Rn", nameEn: "Radon", nameZh: "氡", atomicMass: 222, category: "noble-gas", electronConfiguration: "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p⁶", electronegativity: null, meltingPoint: -71, boilingPoint: -61.7, density: 0.00973, discoveryYear: 1900, description: "放射性惰性气体，可能导致肺癌。", group: 18, period: 6 },
  
  // Period 7
  { atomicNumber: 87, symbol: "Fr", nameEn: "Francium", nameZh: "钫", atomicMass: 223, category: "alkali-metal", electronConfiguration: "[Rn] 7s¹", electronegativity: 0.7, meltingPoint: 27, boilingPoint: 677, density: 1.87, discoveryYear: 1939, description: "最不稳定的天然元素。", group: 1, period: 7 },
  { atomicNumber: 88, symbol: "Ra", nameEn: "Radium", nameZh: "镭", atomicMass: 226, category: "alkaline-earth-metal", electronConfiguration: "[Rn] 7s²", electronegativity: 0.9, meltingPoint: 700, boilingPoint: 1737, density: 5.5, discoveryYear: 1898, description: "放射性元素，曾用于夜光涂料。", group: 2, period: 7 },
  // Actinides (89-103)
  { atomicNumber: 89, symbol: "Ac", nameEn: "Actinium", nameZh: "锕", atomicMass: 227, category: "actinide", electronConfiguration: "[Rn] 6d¹ 7s²", electronegativity: 1.1, meltingPoint: 1050, boilingPoint: 3200, density: 10.07, discoveryYear: 1899, description: "锕系元素之首，高放射性。", group: null, period: 7 },
  { atomicNumber: 90, symbol: "Th", nameEn: "Thorium", nameZh: "钍", atomicMass: 232.04, category: "actinide", electronConfiguration: "[Rn] 6d² 7s²", electronegativity: 1.3, meltingPoint: 1750, boilingPoint: 4788, density: 11.72, discoveryYear: 1829, description: "可能成为未来的核燃料来源。", group: null, period: 7 },
  { atomicNumber: 91, symbol: "Pa", nameEn: "Protactinium", nameZh: "镤", atomicMass: 231.04, category: "actinide", electronConfiguration: "[Rn] 5f² 6d¹ 7s²", electronegativity: 1.5, meltingPoint: 1572, boilingPoint: 4000, density: 15.37, discoveryYear: 1913, description: "稀有放射性元素。", group: null, period: 7 },
  { atomicNumber: 92, symbol: "U", nameEn: "Uranium", nameZh: "铀", atomicMass: 238.03, category: "actinide", electronConfiguration: "[Rn] 5f³ 6d¹ 7s²", electronegativity: 1.38, meltingPoint: 1135, boilingPoint: 4131, density: 18.95, discoveryYear: 1789, description: "用于核能发电和核武器。", group: null, period: 7 },
  { atomicNumber: 93, symbol: "Np", nameEn: "Neptunium", nameZh: "镎", atomicMass: 237, category: "actinide", electronConfiguration: "[Rn] 5f⁴ 6d¹ 7s²", electronegativity: 1.36, meltingPoint: 644, boilingPoint: 4000, density: 20.45, discoveryYear: 1940, description: "第一个超铀元素。", group: null, period: 7 },
  { atomicNumber: 94, symbol: "Pu", nameEn: "Plutonium", nameZh: "钚", atomicMass: 244, category: "actinide", electronConfiguration: "[Rn] 5f⁶ 7s²", electronegativity: 1.28, meltingPoint: 640, boilingPoint: 3228, density: 19.84, discoveryYear: 1940, description: "用于核武器和太空探测器的电源。", group: null, period: 7 },
  { atomicNumber: 95, symbol: "Am", nameEn: "Americium", nameZh: "镅", atomicMass: 243, category: "actinide", electronConfiguration: "[Rn] 5f⁷ 7s²", electronegativity: 1.13, meltingPoint: 1176, boilingPoint: 2011, density: 13.69, discoveryYear: 1944, description: "用于烟雾探测器。", group: null, period: 7 },
  { atomicNumber: 96, symbol: "Cm", nameEn: "Curium", nameZh: "锔", atomicMass: 247, category: "actinide", electronConfiguration: "[Rn] 5f⁷ 6d¹ 7s²", electronegativity: 1.28, meltingPoint: 1345, boilingPoint: 3110, density: 13.51, discoveryYear: 1944, description: "以居里夫妇命名。", group: null, period: 7 },
  { atomicNumber: 97, symbol: "Bk", nameEn: "Berkelium", nameZh: "锫", atomicMass: 247, category: "actinide", electronConfiguration: "[Rn] 5f⁹ 7s²", electronegativity: 1.3, meltingPoint: 1050, boilingPoint: 2627, density: 14.79, discoveryYear: 1949, description: "以加州伯克利命名。", group: null, period: 7 },
  { atomicNumber: 98, symbol: "Cf", nameEn: "Californium", nameZh: "锎", atomicMass: 251, category: "actinide", electronConfiguration: "[Rn] 5f¹⁰ 7s²", electronegativity: 1.3, meltingPoint: 900, boilingPoint: 1470, density: 15.1, discoveryYear: 1950, description: "用于便携式中子源。", group: null, period: 7 },
  { atomicNumber: 99, symbol: "Es", nameEn: "Einsteinium", nameZh: "锿", atomicMass: 252, category: "actinide", electronConfiguration: "[Rn] 5f¹¹ 7s²", electronegativity: 1.3, meltingPoint: 860, boilingPoint: 996, density: 8.84, discoveryYear: 1952, description: "以爱因斯坦命名。", group: null, period: 7 },
  { atomicNumber: 100, symbol: "Fm", nameEn: "Fermium", nameZh: "镄", atomicMass: 257, category: "actinide", electronConfiguration: "[Rn] 5f¹² 7s²", electronegativity: 1.3, meltingPoint: 1527, boilingPoint: null, density: null, discoveryYear: 1952, description: "以费米命名。", group: null, period: 7 },
  { atomicNumber: 101, symbol: "Md", nameEn: "Mendelevium", nameZh: "钔", atomicMass: 258, category: "actinide", electronConfiguration: "[Rn] 5f¹³ 7s²", electronegativity: 1.3, meltingPoint: 827, boilingPoint: null, density: null, discoveryYear: 1955, description: "以门捷列夫命名。", group: null, period: 7 },
  { atomicNumber: 102, symbol: "No", nameEn: "Nobelium", nameZh: "锘", atomicMass: 259, category: "actinide", electronConfiguration: "[Rn] 5f¹⁴ 7s²", electronegativity: 1.3, meltingPoint: 827, boilingPoint: null, density: null, discoveryYear: 1958, description: "以诺贝尔命名。", group: null, period: 7 },
  { atomicNumber: 103, symbol: "Lr", nameEn: "Lawrencium", nameZh: "铹", atomicMass: 262, category: "actinide", electronConfiguration: "[Rn] 5f¹⁴ 7s² 7p¹", electronegativity: 1.3, meltingPoint: 1627, boilingPoint: null, density: null, discoveryYear: 1961, description: "锕系元素中最后一个。", group: null, period: 7 },
  { atomicNumber: 104, symbol: "Rf", nameEn: "Rutherfordium", nameZh: "𬬻", atomicMass: 267, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d² 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1964, description: "以卢瑟福命名的人造元素。", group: 4, period: 7 },
  { atomicNumber: 105, symbol: "Db", nameEn: "Dubnium", nameZh: "𬭊", atomicMass: 268, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d³ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1967, description: "以杜布纳命名。", group: 5, period: 7 },
  { atomicNumber: 106, symbol: "Sg", nameEn: "Seaborgium", nameZh: "𬭳", atomicMass: 269, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d⁴ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1974, description: "以西博格命名。", group: 6, period: 7 },
  { atomicNumber: 107, symbol: "Bh", nameEn: "Bohrium", nameZh: "𬭛", atomicMass: 270, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d⁵ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1981, description: "以玻尔命名。", group: 7, period: 7 },
  { atomicNumber: 108, symbol: "Hs", nameEn: "Hassium", nameZh: "𬭶", atomicMass: 277, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d⁶ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1984, description: "以黑森州命名。", group: 8, period: 7 },
  { atomicNumber: 109, symbol: "Mt", nameEn: "Meitnerium", nameZh: "鿏", atomicMass: 278, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d⁷ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1982, description: "以迈特纳命名。", group: 9, period: 7 },
  { atomicNumber: 110, symbol: "Ds", nameEn: "Darmstadtium", nameZh: "𫟼", atomicMass: 281, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d⁸ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1994, description: "以达姆施塔特命名。", group: 10, period: 7 },
  { atomicNumber: 111, symbol: "Rg", nameEn: "Roentgenium", nameZh: "𬬭", atomicMass: 282, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d⁹ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1994, description: "以伦琴命名。", group: 11, period: 7 },
  { atomicNumber: 112, symbol: "Cn", nameEn: "Copernicium", nameZh: "鿔", atomicMass: 285, category: "transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1996, description: "以哥白尼命名。", group: 12, period: 7 },
  { atomicNumber: 113, symbol: "Nh", nameEn: "Nihonium", nameZh: "鿭", atomicMass: 286, category: "post-transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s² 7p¹", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 2003, description: "日本发现，意为'日本'。", group: 13, period: 7 },
  { atomicNumber: 114, symbol: "Fl", nameEn: "Flerovium", nameZh: "𫓧", atomicMass: 289, category: "post-transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s² 7p²", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 1999, description: "以弗廖罗夫命名。", group: 14, period: 7 },
  { atomicNumber: 115, symbol: "Mc", nameEn: "Moscovium", nameZh: "镆", atomicMass: 290, category: "post-transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s² 7p³", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 2003, description: "以莫斯科命名。", group: 15, period: 7 },
  { atomicNumber: 116, symbol: "Lv", nameEn: "Livermorium", nameZh: "𫟷", atomicMass: 293, category: "post-transition-metal", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s² 7p⁴", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 2000, description: "以利弗莫尔命名。", group: 16, period: 7 },
  { atomicNumber: 117, symbol: "Ts", nameEn: "Tennessine", nameZh: "鿬", atomicMass: 294, category: "halogen", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s² 7p⁵", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 2010, description: "以田纳西州命名。", group: 17, period: 7 },
  { atomicNumber: 118, symbol: "Og", nameEn: "Oganesson", nameZh: "鿫", atomicMass: 294, category: "noble-gas", electronConfiguration: "[Rn] 5f¹⁴ 6d¹⁰ 7s² 7p⁶", electronegativity: null, meltingPoint: null, boilingPoint: null, density: null, discoveryYear: 2002, description: "目前已知最重的元素。", group: 18, period: 7 },
]

// Helper to get element position in the periodic table grid
export function getElementPosition(element: Element): { row: number; col: number } | null {
  if (element.category === "lanthanide") {
    return { row: 9, col: element.atomicNumber - 57 + 3 }
  }
  if (element.category === "actinide") {
    return { row: 10, col: element.atomicNumber - 89 + 3 }
  }
  if (element.group === null) return null
  return { row: element.period, col: element.group }
}

export function getElementByAtomicNumber(atomicNumber: number): Element | undefined {
  return elements.find(e => e.atomicNumber === atomicNumber)
}

export function searchElements(query: string): Element[] {
  const q = query.toLowerCase()
  return elements.filter(e => 
    e.symbol.toLowerCase().includes(q) ||
    e.nameEn.toLowerCase().includes(q) ||
    e.nameZh.includes(q) ||
    e.atomicNumber.toString() === q
  )
}
