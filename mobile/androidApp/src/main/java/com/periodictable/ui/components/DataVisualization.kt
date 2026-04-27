package com.periodictable.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.categoryInfo
import com.periodictable.data.elements
import kotlin.math.roundToInt

enum class PropertyKey(
    val displayName: String,
    val unit: String,
    val getValue: (Element) -> Double?
) {
    ATOMIC_MASS(
        displayName = "原子质量",
        unit = "u",
        getValue = { it.atomicMass }
    ),
    ELECTRONEGATIVITY(
        displayName = "电负性",
        unit = "",
        getValue = { it.electronegativity }
    ),
    MELTING_POINT(
        displayName = "熔点",
        unit = "°C",
        getValue = { it.meltingPoint }
    ),
    BOILING_POINT(
        displayName = "沸点",
        unit = "°C",
        getValue = { it.boilingPoint }
    ),
    DENSITY(
        displayName = "密度",
        unit = "g/cm³",
        getValue = { it.density }
    )
}

enum class ComparisonMode {
    TOP_BOTTOM, TREND
}

@Composable
fun DataVisualization(
    modifier: Modifier = Modifier
) {
    var selectedProperty by remember { mutableStateOf(PropertyKey.ELECTRONEGATIVITY) }
    var comparisonMode by remember { mutableStateOf(ComparisonMode.TOP_BOTTOM) }

    val validElements = remember(selectedProperty) {
        elements.filter { selectedProperty.getValue(it) != null }
    }

    val sortedElements = remember(validElements, selectedProperty) {
        validElements.sortedByDescending { selectedProperty.getValue(it)!! }
    }

    val top10 = sortedElements.take(10)
    val bottom10 = sortedElements.takeLast(10).reversed()

    val maxValue = sortedElements.firstOrNull()?.let { selectedProperty.getValue(it) } ?: 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF06B6D4).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📊", fontSize = 16.sp)
            }
            Column {
                Text(
                    text = "数据可视化",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "比较元素属性和趋势",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
            }
        }

        // 属性选择器
        Text(
            text = "选择属性",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            PropertyKey.values().forEach { property ->
                val isSelected = selectedProperty == property
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.2f)
                            else Color.White.copy(alpha = 0.05f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.5f)
                            else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedProperty = property },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = property.displayName,
                        color = if (isSelected) Color(0xFF06B6D4)
                        else Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 视图模式切换
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModeButton(
                text = "⚡ 前10/后10",
                isSelected = comparisonMode == ComparisonMode.TOP_BOTTOM,
                onClick = { comparisonMode = ComparisonMode.TOP_BOTTOM },
                modifier = Modifier.weight(1f)
            )
            ModeButton(
                text = "📈 周期趋势",
                isSelected = comparisonMode == ComparisonMode.TREND,
                onClick = { comparisonMode = ComparisonMode.TREND },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 可视化内容
        if (comparisonMode == ComparisonMode.TOP_BOTTOM) {
            TopBottomView(
                topElements = top10,
                bottomElements = bottom10,
                maxValue = maxValue,
                selectedProperty = selectedProperty
            )
        } else {
            TrendView(
                elements = validElements,
                maxValue = maxValue,
                selectedProperty = selectedProperty
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.2f)
                else Color.White.copy(alpha = 0.05f)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.5f)
                else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF06B6D4)
            else Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun TopBottomView(
    topElements: List<Element>,
    bottomElements: List<Element>,
    maxValue: Double,
    selectedProperty: PropertyKey
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top 10
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${selectedProperty.displayName} 最高",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                topElements.forEachIndexed { index, element ->
                    ElementBarItem(
                        element = element,
                        value = selectedProperty.getValue(element)!!,
                        maxValue = maxValue,
                        rank = index + 1
                    )
                }
            }
        }

        // Bottom 10
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${selectedProperty.displayName} 最低",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                bottomElements.forEachIndexed { index, element ->
                    ElementBarItem(
                        element = element,
                        value = selectedProperty.getValue(element)!!,
                        maxValue = maxValue,
                        rank = topElements.size + index + 1,
                        isBottom = true
                    )
                }
            }
        }
    }
}

@Composable
private fun ElementBarItem(
    element: Element,
    value: Double,
    maxValue: Double,
    rank: Int,
    isBottom: Boolean = false
) {
    val categoryData = categoryInfo[element.category]!!
    val percentage = (value / maxValue).coerceIn(0.05, 1.0).toFloat()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = rank.toString(),
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 10.sp,
            modifier = Modifier.width(14.dp)
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(categoryData.color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = element.symbol,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = element.nameZh,
                    color = Color.White,
                    fontSize = 11.sp
                )
                Text(
                    text = String.format("%.2f", value),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 9.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                val animWidth by animateFloatAsState(
                    targetValue = percentage,
                    animationSpec = tween(durationMillis = 500),
                    label = "bar-width"
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animWidth)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(categoryData.color)
                )
            }
        }
    }
}

@Composable
private fun TrendView(
    elements: List<Element>,
    maxValue: Double,
    selectedProperty: PropertyKey
) {
    // 按周期分组
    val elementsByPeriod = remember(elements) {
        elements.groupBy { it.period }
            .mapValues { (_, elementsInPeriod) ->
                elementsInPeriod.sortedBy { it.group ?: 0 }
            }
    }

    Column {
        Text(
            text = "${selectedProperty.displayName} 随周期变化趋势",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..7).forEach { period ->
                val periodElements = elementsByPeriod[period] ?: emptyList()
                if (periodElements.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "P$period",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 10.sp,
                            modifier = Modifier.width(24.dp)
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            periodElements.forEach { element ->
                                val value = selectedProperty.getValue(element)!!
                                val normalizedHeight = (value / maxValue).coerceIn(0.1, 1.0).toFloat()
                                val categoryData = categoryInfo[element.category]!!

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    val animHeight by animateFloatAsState(
                                        targetValue = normalizedHeight * 50.dp.value,
                                        animationSpec = tween(durationMillis = 500),
                                        label = "trend-bar-height"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(16.dp)
                                            .height(animHeight.dp)
                                            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                            .background(categoryData.color)
                                    )
                                    Text(
                                        text = element.symbol,
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 7.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "高度表示 ${selectedProperty.displayName} 相对值",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
