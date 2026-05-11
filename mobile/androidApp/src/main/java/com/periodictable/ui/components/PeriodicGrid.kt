package com.periodictable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
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
@NonRestartableComposable  // 告诉编译器无需支持部分重组，大幅减少编译开销
fun PeriodicGrid(
    highlightedAtomicNumbers: Set<Int>,
    onElementClick: (Element) -> Unit,
    horizontalScrollState: ScrollState,
    verticalScrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    fun isElementHighlighted(element: Element): Boolean = element.atomicNumber in highlightedAtomicNumbers

    // 缓存网格数据，避免每次重组重新计算
    val (grid) = remember {
        val mainElements = elements.filter {
            it.category != ElementCategory.LANTHANIDE &&
            it.category != ElementCategory.ACTINIDE
        }

        val rows = 9
        val cols = 18
        val gridData: List<MutableList<Element?>> = List(rows) { MutableList<Element?>(cols) { null } }

        mainElements.forEach { element ->
            val row = element.period - 1
            val col = element.group?.minus(1)
            if (col != null && row < 7) {
                gridData[row][col] = element
            }
        }

        // 放置镧系和锕系元素
        elements.filter { it.category == ElementCategory.LANTHANIDE }.forEachIndexed { index, element ->
            if (3 + index < cols) {
                gridData[7][3 + index] = element
            }
        }
        elements.filter { it.category == ElementCategory.ACTINIDE }.forEachIndexed { index, element ->
            if (3 + index < cols) {
                gridData[8][3 + index] = element
            }
        }

        Triple(gridData, rows, cols)
    }

    val cellSize = 48 // 放大单元格尺寸，方便点击
    val gridWidth = cellSize * 18 + 24 // 18列 + 左侧序号宽度

    // PHASE 0 验证2: 外层单次横向 + 纵向滚动（移除9个Row各自的horizontalScroll）
    Column(
        modifier = modifier
            .width(gridWidth.dp)
            .horizontalScroll(horizontalScrollState)
            .verticalScroll(verticalScrollState)
    ) {
        // 族号头
        Row(
            modifier = Modifier.padding(start = 24.dp)
        ) {
            for (i in 1..18) {
                Box(
                    modifier = Modifier.size(cellSize.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = i.toString(),
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 9.sp
                    )
                }
            }
        }

        // 主表
        for (row in 0..6) {
            Row(
                modifier = Modifier.padding(vertical = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Box(
                    modifier = Modifier.size(24.dp, cellSize.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (row + 1).toString(),
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 9.sp
                    )
                }
                for (col in 0..17) {
                    val element = grid[row][col]
                    when {
                        element != null -> {
                            ElementCard(
                                element = element,
                                isHighlighted = isElementHighlighted(element),
                                onClick = onElementClick,
                                cellSize = cellSize
                            )
                        }
                        // 镧系占位标记 (第5行第3列)
                        row == 5 && col == 2 -> {
                            LanthanidePlaceholder(text = "57-71", color = Color(0xFFEC4899), cellSize = cellSize)
                        }
                        // 锕系占位标记 (第6行第3列)
                        row == 6 && col == 2 -> {
                            LanthanidePlaceholder(text = "89-103", color = Color(0xFFF43F5E), cellSize = cellSize)
                        }
                        else -> {
                            Spacer(modifier = Modifier.size(cellSize.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 镧系和锕系
        for (row in 7..8) {
            Row(
                modifier = Modifier.padding(vertical = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Box(
                    modifier = Modifier.size(24.dp, cellSize.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (row == 7) "*" else "**",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.width((cellSize * 3 + 2).dp))
                for (col in 3..17) {
                    val element = grid[row][col]
                    if (element != null) {
                        ElementCard(
                            element = element,
                            isHighlighted = isElementHighlighted(element),
                            onClick = onElementClick,
                            cellSize = cellSize
                        )
                    } else {
                        Spacer(modifier = Modifier.size(cellSize.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LanthanidePlaceholder(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    cellSize: Int = 42
) {
    Box(
        modifier = modifier
            .size(cellSize.dp)
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
