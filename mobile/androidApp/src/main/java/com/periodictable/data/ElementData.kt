package com.periodictable.data

import androidx.compose.ui.graphics.Color

data class Element(
    val atomicNumber: Int,
    val symbol: String,
    val nameEn: String,
    val nameZh: String,
    val atomicMass: Double,
    val category: ElementCategory,
    val electronConfiguration: String,
    val electronegativity: Double? = null,
    val meltingPoint: Double? = null,
    val boilingPoint: Double? = null,
    val density: Double? = null,
    val discoveryYear: Int? = null,
    val description: String,
    val group: Int? = null,
    val period: Int
)

enum class ElementCategory {
    ALKALI_METAL,
    ALKALINE_EARTH_METAL,
    TRANSITION_METAL,
    POST_TRANSITION_METAL,
    METALLOID,
    NONMETAL,
    HALOGEN,
    NOBLE_GAS,
    LANTHANIDE,
    ACTINIDE
}

data class CategoryInfo(
    val nameZh: String,
    val nameEn: String,
    val color: Color,
    val glowColor: Color
)

val categoryInfo: Map<ElementCategory, CategoryInfo> = mapOf(
    ElementCategory.ALKALI_METAL to CategoryInfo(
        nameZh = "碱金属",
        nameEn = "Alkali Metal",
        color = Color(0xFFEF4444),
        glowColor = Color(0x80EF4444)
    ),
    ElementCategory.ALKALINE_EARTH_METAL to CategoryInfo(
        nameZh = "碱土金属",
        nameEn = "Alkaline Earth",
        color = Color(0xFFF97316),
        glowColor = Color(0x80F97316)
    ),
    ElementCategory.TRANSITION_METAL to CategoryInfo(
        nameZh = "过渡金属",
        nameEn = "Transition Metal",
        color = Color(0xFFEAB308),
        glowColor = Color(0x80EAB308)
    ),
    ElementCategory.POST_TRANSITION_METAL to CategoryInfo(
        nameZh = "后过渡金属",
        nameEn = "Post-transition",
        color = Color(0xFF22C55E),
        glowColor = Color(0x8022C55E)
    ),
    ElementCategory.METALLOID to CategoryInfo(
        nameZh = "准金属",
        nameEn = "Metalloid",
        color = Color(0xFF14B8A6),
        glowColor = Color(0x8014B8A6)
    ),
    ElementCategory.NONMETAL to CategoryInfo(
        nameZh = "非金属",
        nameEn = "Nonmetal",
        color = Color(0xFF06B6D4),
        glowColor = Color(0x8006B6D4)
    ),
    ElementCategory.HALOGEN to CategoryInfo(
        nameZh = "卤素",
        nameEn = "Halogen",
        color = Color(0xFF3B82F6),
        glowColor = Color(0x803B82F6)
    ),
    ElementCategory.NOBLE_GAS to CategoryInfo(
        nameZh = "惰性气体",
        nameEn = "Noble Gas",
        color = Color(0xFF6366F1),
        glowColor = Color(0x806366F1)
    ),
    ElementCategory.LANTHANIDE to CategoryInfo(
        nameZh = "镧系元素",
        nameEn = "Lanthanide",
        color = Color(0xFFEC4899),
        glowColor = Color(0x80EC4899)
    ),
    ElementCategory.ACTINIDE to CategoryInfo(
        nameZh = "锕系元素",
        nameEn = "Actinide",
        color = Color(0xFFF43F5E),
        glowColor = Color(0x80F43F5E)
    )
)

fun getElementPosition(element: Element): Pair<Int, Int>? {
    if (element.category == ElementCategory.LANTHANIDE) {
        return Pair(8, element.atomicNumber - 57 + 3)
    }
    if (element.category == ElementCategory.ACTINIDE) {
        return Pair(9, element.atomicNumber - 89 + 3)
    }
    if (element.group == null) return null
    return Pair(element.period - 1, element.group - 1)
}
