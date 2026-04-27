package com.periodictable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.ElementCategory
import com.periodictable.data.elements

@Composable
fun PeriodicGrid(
    selectedCategories: Set<ElementCategory>,
    searchQuery: String,
    onElementClick: (Element) -> Unit,
    modifier: Modifier = Modifier
) {
    fun isElementHighlighted(element: Element): Boolean {
        if (selectedCategories.isNotEmpty() && element.category !in selectedCategories) {
            return false
        }
        if (searchQuery.isNotEmpty()) {
            val q = searchQuery.lowercase()
            return element.symbol.lowercase().contains(q) ||
                    element.nameZh.contains(q) ||
                    element.nameEn.lowercase().contains(q) ||
                    element.atomicNumber.toString() == q
        }
        return true
    }

    val mainElements = elements.filter {
        it.category != ElementCategory.LANTHANIDE &&
        it.category != ElementCategory.ACTINIDE
    }

    val gridRows = 9
    val gridCols = 18
    val grid: List<MutableList<Element?>> = List(gridRows) { MutableList<Element?>(gridCols) { null } }

    mainElements.forEach { element ->
        val row = element.period - 1
        val col = element.group?.minus(1)
        if (col != null && row < 7) {
            grid[row][col] = element
        }
    }

    // 放置镧系和锕系元素
    elements.filter { it.category == ElementCategory.LANTHANIDE }.forEachIndexed { index, element ->
        if (3 + index < gridCols) {
            grid[7][3 + index] = element
        }
    }
    elements.filter { it.category == ElementCategory.ACTINIDE }.forEachIndexed { index, element ->
        if (3 + index < gridCols) {
            grid[8][3 + index] = element
        }
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        // 族号头
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 24.dp)
        ) {
            for (i in 1..18) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = i.toString(),
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 主表
        for (row in 0..6) {
            Row(modifier = Modifier.padding(vertical = 1.dp)) {
                Box(
                    modifier = Modifier.size(24.dp, 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (row + 1).toString(),
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp
                    )
                }
                for (col in 0..17) {
                    val element = grid[row][col]
                    when {
                        element != null -> {
                            ElementCard(
                                element = element,
                                isHighlighted = isElementHighlighted(element),
                                onClick = onElementClick
                            )
                        }
                        // 镧系占位标记 (第5行第3列)
                        row == 5 && col == 2 -> {
                            LanthanidePlaceholder(text = "57-71", color = Color(0xFFEC4899))
                        }
                        // 锕系占位标记 (第6行第3列)
                        row == 6 && col == 2 -> {
                            LanthanidePlaceholder(text = "89-103", color = Color(0xFFF43F5E))
                        }
                        else -> {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 镧系和锕系
        for (row in 7..8) {
            Row(modifier = Modifier.padding(vertical = 1.dp)) {
                Box(
                    modifier = Modifier.size(24.dp, 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (row == 7) "*" else "**",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.size(48.dp * 3))
                for (col in 3..17) {
                    val element = grid[row][col]
                    if (element != null) {
                        ElementCard(
                            element = element,
                            isHighlighted = isElementHighlighted(element),
                            onClick = onElementClick
                        )
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LanthanidePlaceholder(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .drawWithCache {
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                onDrawWithContent {
                    drawContent()
                    drawRoundRect(
                        color = color.copy(alpha = 0.4f),
                        style = Stroke(width = 1.dp.toPx(), pathEffect = pathEffect)
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color.copy(alpha = 0.7f),
            fontSize = 9.sp
        )
    }
}
