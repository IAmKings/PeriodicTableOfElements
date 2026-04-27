package com.periodictable.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.categoryInfo

@Composable
fun ElementCard(
    element: Element,
    isHighlighted: Boolean,
    onClick: (Element) -> Unit,
    modifier: Modifier = Modifier,
    cellSize: Int = 48
) {
    val categoryData = categoryInfo[element.category]!!
    var isPressed by remember { mutableStateOf(false) }

    // 缩放动画
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "card-scale"
    )

    // 透明度动画
    val alpha by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.2f,
        animationSpec = tween(durationMillis = 300),
        label = "card-alpha"
    )

    // 发光强度动画
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed && isHighlighted) 0.6f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "glow-alpha"
    )

    Box(
        modifier = modifier
            .size(cellSize.dp)
            .scale(scale)
    ) {
        // 发光背景
        if (isPressed && isHighlighted) {
            Box(
                modifier = Modifier
                    .size(cellSize.dp)
                    .blur(
                        radiusX = 12.dp,
                        radiusY = 12.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    .background(categoryData.color.copy(alpha = glowAlpha * 0.5f))
            )
        }

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
                    isPressed = true
                    onClick(element)
                    isPressed = false
                }
                .padding(4.dp)
        ) {
            Text(
                text = element.atomicNumber.toString(),
                color = Color.White.copy(alpha = 0.7f * alpha),
                fontSize = 8.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = element.symbol,
                    color = Color.White.copy(alpha = alpha),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = element.nameZh,
                    color = Color.White.copy(alpha = 0.8f * alpha),
                    fontSize = 9.sp
                )
            }
        }
    }
}
