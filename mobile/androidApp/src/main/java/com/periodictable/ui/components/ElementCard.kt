package com.periodictable.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
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

    // 发光强度动画
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.6f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "glow-alpha"
    )

    Box(
        modifier = modifier.size(cellSize.dp)
    ) {
        // 发光背景
        AnimatedVisibility(
            visible = isPressed,
            enter = scaleIn(animationSpec = tween(200)),
            exit = scaleOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                .scale(scale)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (isHighlighted) categoryData.color
                    else categoryData.color.copy(alpha = 0.85f)
                )
                .border(
                    width = 1.dp,
                    color = if (isHighlighted) Color.White.copy(alpha = 0.3f)
                    else Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null // 移除默认波纹效果，使用自定义动画
                ) {
                    onClick(element)
                }
                .padding(4.dp)
        ) {
            Text(
                text = element.atomicNumber.toString(),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 8.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = element.symbol,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = element.nameZh,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 9.sp
                )
            }
        }
    }
}
