package com.periodictable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.categoryInfo

@Composable
@NonRestartableComposable  // 118 个卡片 × 减少每个的编译开销
fun ElementCard(
    element: Element,
    isHighlighted: Boolean,
    onClick: (Element) -> Unit,
    modifier: Modifier = Modifier,
    cellSize: Int = 48
) {
    val categoryData = categoryInfo[element.category]!!

    // 透明度 - 无动画避免切换卡顿
    val alpha = if (isHighlighted) 1f else 0.2f

    Box(
        modifier = modifier
            .size(cellSize.dp)
    ) {

        Box(
            modifier = Modifier
                .size(cellSize.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    color = categoryData.color.copy(alpha = alpha * if (isHighlighted) 1f else 0.5f)
                )
                .border(
                    width = 1.dp,
                    color = if (isHighlighted) Color.White.copy(alpha = 0.3f * alpha)
                    else Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = isHighlighted
                ) {
                    onClick(element)
                }
                .padding(3.dp)
        ) {
             Text(
                 text = element.atomicNumber.toString(),
                 color = Color.White.copy(alpha = 0.7f * alpha),
                 fontSize = 7.sp,
                 modifier = Modifier
                     .align(Alignment.TopStart)
                     .offset(y = (-4).dp)
                     .padding(start = 1.dp)
             )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = element.symbol,
                    color = Color.White.copy(alpha = alpha),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = element.nameZh,
                    color = Color.White.copy(alpha = 0.8f * alpha),
                    fontSize = 8.sp
                )
            }
        }
    }
}
