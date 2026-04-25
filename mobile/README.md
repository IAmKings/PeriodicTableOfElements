# 元素周期表 - Compose Multiplatform Android

基于 **Kotlin + Compose Multiplatform** 构建的 Android 元素周期表应用。

---

## 功能特性

- ✨ **周期表展示** - 标准 18×7 周期表布局
- 🔍 **搜索功能** - 按元素符号、中文名、英文名、原子序数快速搜索
- 🏷️ **分类筛选** - 按 10 种元素分类筛选（碱金属、过渡金属、非金属等）
- 📋 **元素详情** - 点击元素查看完整属性信息（电子排布、电负性、熔沸点、密度等）
- 🎨 **精美动效** - 分类配色、悬停发光效果
- 📱 **自适应** - 支持横竖屏切换

---

## 技术栈

| 技术 | 说明 |
|------|------|
| **Kotlin** | 编程语言 |
| **Compose Multiplatform 1.6.11** | UI 框架 |
| **Material 3** | Material Design 3 组件库 |
| **Jetpack Compose** | Android UI 框架 |

---

## 项目结构

```
mobile/
├── shared/                      # 共享代码模块
│   └── src/
│       └── commonMain/         # 通用代码
│           └── kotlin/com/periodictable/
│               ├── data/     # 数据模型和元素数据
│               ├── ui/
│               │   ├── theme/ # 主题和颜色定义
│               │   ├── components/ #  UI 组件
│               │   └── screens/    #  屏幕
│               └── App.kt    #  App 入口
└── androidApp/                 # Android 应用
    └── src/main/
        └── MainActivity.kt
```

---

## 元素分类配色

| 分类 | 颜色 | 说明 |
|------|------|------|
| 碱金属 | 🔴 红色 | Alkali Metal |
| 碱土金属 | 🟠 橙色 | Alkaline Earth |
| 过渡金属 | 🟡 黄色 | Transition Metal |
| 后过渡金属 | 🟢 绿色 | Post-transition Metal |
| 准金属 | 🟦 青色 | Metalloid |
| 非金属 | 🔵 蓝色 | Nonmetal |
| 卤素 | 🟣 靛蓝 | Halogen |
| 稀有气体 | 🟪 紫色 | Noble Gas |
| 镧系元素 | 🔴 粉色 | Lanthanide |
| 锕系元素 | 💗 玫瑰红 | Actinide |

---

## 运行项目

### Android

```bash
cd mobile
./gradlew androidApp:installDebug
```

或者在 Android Studio 中打开 `mobile/` 目录，选择 `androidApp` 运行配置。

---

## 开发环境要求

- JDK 11+
- Android Studio Hedgehog (2023.1.1) 或更高版本
- Kotlin 2.0.0
- Android SDK 34

---

## 相关链接

- [Compose Multiplatform 官方文档](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Kotlin Multiplatform 官方文档](https://kotlinlang.org/docs/multiplatform.html)
