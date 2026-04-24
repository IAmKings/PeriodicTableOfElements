# 🧪 交互式元素周期表 | Periodic Table of Elements

一个现代化的交互式元素周期表应用，支持中英双语，包含搜索筛选、测验学习和数据可视化功能。

[![Periodic Table](https://img.shields.io/badge/Periodic%20Table-118%20Elements-blue)]
[![Next.js 16](https://img.shields.io/badge/Next.js-16-black)]
[![React 19](https://img.shields.io/badge/React-19-61dafb)]
[![Tailwind CSS 4](https://img.shields.io/badge/Tailwind%20CSS-4-38bdf8)]

---

## ✨ 功能特点

### 📋 核心功能
- **完整元素数据**：包含 118 个化学元素的详细信息
- **中英双语**：所有元素均支持中文和英文名称显示
- **响应式设计**：完美适配桌面端和移动端
- **深色主题**：深空科幻风格，护眼舒适

### 🔍 三种视图模式
| 模式 | 功能描述 |
|------|----------|
| **周期表视图** | 标准 18×7 周期表布局，支持搜索和分类筛选，点击查看元素详情 |
| **测验模式** | 随机生成 4 种题型（符号、中文名、分类、原子序数），计分 + 连击系统 |
| **数据可视化** | 5 种属性趋势对比（原子质量、电负性、熔沸点、密度），支持趋势图 / Top 10 对比 |

### 🎯 交互特性
- **实时搜索**：按符号、中文名、英文名、原子序数快速搜索
- **分类筛选**：10 种元素分类，点击图例即可筛选显示
- **联动高亮**：可视化视图鼠标悬停时周期表对应元素高亮
- **元素详情**：点击元素弹出详情卡片，包含电子层分布动画
- **悬停动效**：元素卡片悬停时放大 + 发光效果

---

## 🛠 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| **Next.js** | 16.2.4 | React 框架，App Router |
| **React** | 19.0 | UI 库 |
| **TypeScript** | 5.7.3 | 类型安全 |
| **Tailwind CSS** | 4.2.0 | 样式框架 |
| **shadcn/ui** | - | UI 组件库 |
| **Lucide React** | 0.564.0 | 图标库 |
| **Recharts** | 2.15.0 | 数据可视化图表 |
| **Geist** | - | Vercel 官方字体 |

---

## 🚀 快速开始

### 环境要求
- Node.js 18+
- pnpm (推荐) 或 npm / yarn

### 安装依赖
```bash
cd web
pnpm install
```

### 开发模式
```bash
pnpm dev
```
访问 `http://localhost:3000`

### 构建生产版本
```bash
pnpm build
pnpm start
```

### 代码检查
```bash
pnpm lint
```

---

## 📁 项目结构

```
web/
├── app/
│   ├── layout.tsx          # 根布局配置
│   └── page.tsx            # 主页面 - 周期表应用入口
│
├── components/
│   ├── periodic-table/     # 核心业务组件
│   │   ├── periodic-grid.tsx      # 周期表网格布局
│   │   ├── element-card.tsx       # 元素卡片组件
│   │   ├── element-detail.tsx     # 元素详情弹窗
│   │   ├── search-filter.tsx      # 搜索筛选组件
│   │   ├── quiz-mode.tsx          # 测验模式
│   │   ├── data-visualization.tsx # 数据可视化
│   │   └── electron-shell.tsx     # 电子层分布动画
│   │
│   └── ui/                 # shadcn/ui 基础组件 (50+)
│
├── lib/
│   ├── elements-data.ts    # 118 元素完整数据 + 类型定义
│   └── utils.ts            # cn() 工具函数
│
├── hooks/                  # React Hooks
│   ├── use-toast.ts
│   └── use-mobile.ts
│
├── public/                 # 静态资源
│   └── icon.svg
│
└── styles/
    └── globals.css         # Tailwind 4 配置 + CSS 变量
```

---

## 🧪 元素数据结构

每个元素包含以下完整属性：

```typescript
interface Element {
  atomicNumber: number          // 原子序数 (1-118)
  symbol: string                // 元素符号 (H, He, Li...)
  nameZh: string                // 中文名称
  nameEn: string                // 英文名称
  atomicMass: number            // 原子质量
  category: ElementCategory     // 元素分类
  electronConfiguration: string // 电子排布式
  electronegativity: number | null  // 电负性
  meltingPoint: number | null   // 熔点 (°C)
  boilingPoint: number | null   // 沸点 (°C)
  density: number | null        // 密度 (g/cm³)
  discoveryYear: number | null  // 发现年份
  description: string           // 元素描述
  period: number                // 周期
  group: number | null          // 族
}
```

### 元素分类
1. 🟥 碱金属 (Alkali Metal)
2. 🟧 碱土金属 (Alkaline Earth)
3. 🟨 过渡金属 (Transition Metal)
4. 🟩 后过渡金属 (Post-transition Metal)
5. 🟦 准金属 (Metalloid)
6. 🟦 非金属 (Nonmetal)
7. 🟦 卤素 (Halogen)
8. 🟪 稀有气体 (Noble Gas)
9. 🩷 镧系元素 (Lanthanide)
10. 🌹 锕系元素 (Actinide)

---

## 🎨 设计特点

### 视觉风格
- **深空背景**：`slate-950` 深色主题
- **发光效果**：元素卡片悬停时发光动画
- **脉动背景**：背景光晕缓慢呼吸效果
- **像素级对齐**：周期表采用精确的网格布局

### 交互设计
- **流畅动画**：所有状态切换均有过渡动效
- **即时反馈**：搜索、筛选、点击实时响应
- **触控友好**：支持移动端触摸操作
- **无障碍**：语义化 HTML，键盘可访问

---

## 🔧 核心组件 API

### PeriodicGrid
周期表网格主组件
```tsx
<PeriodicGrid
  selectedCategories={Set<ElementCategory>}
  searchQuery={string}
  highlightedElements={Set<number>}
  onElementClick={(element: Element) => void}
  onCategoriesChange={(categories: Set<ElementCategory>) => void}
/>
```

### ElementDetail
元素详情弹窗
```tsx
<ElementDetail
  element={Element | null}
  onClose={() => void}
/>
```

### QuizMode
测验模式组件
```tsx
<QuizMode />
```

### DataVisualization
数据可视化组件
```tsx
<DataVisualization
  onHighlight={(elements: Set<number>) => void}
/>
```

---

## 📊 数据来源

本项目数据基于以下公开资料整理：
- 维基百科 (中文/英文) 元素条目
- PubChem 公共化学数据库
- IUPAC 官方元素数据

---

## 🤝 开发说明

### 代码规范
- TypeScript Strict 模式
- ESLint 代码检查
- 组件采用 PascalCase 命名
- 文件使用 kebab-case 命名

### 扩展开发
1. **添加新元素属性**：在 `lib/elements-data.ts` 中扩展数据结构
2. **添加新视图模式**：在主页面添加新的 Tab 视图
3. **自定义主题**：修改 `styles/globals.css` 中的 CSS 变量
4. **新增图表类型**：在 `data-visualization.tsx` 中扩展 Recharts 配置

---

## 📝 待办功能

- [ ] PWA 离线支持
- [ ] 元素收藏功能
- [ ] 更多化学方程式
- [ ] 元素同位素数据
- [ ] 电离能趋势图
- [ ] 导出元素数据
- [ ] 多语言切换 (i18n)

---

## 📄 许可证

MIT License

---

<p align="center">
  Made with ⚛️ React + 🧪 Chemistry
</p>
