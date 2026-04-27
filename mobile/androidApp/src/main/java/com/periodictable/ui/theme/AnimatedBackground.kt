package com.periodictable.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier
) {
    val transition1 = rememberInfiniteTransition(label = "pulse1")
    val transition2 = rememberInfiniteTransition(label = "pulse2")
    val transition3 = rememberInfiniteTransition(label = "pulse3")

    val alpha1 by transition1.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )

    val alpha2 by transition2.animateFloat(
        initialValue = 0.06f,
        targetValue = 0.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )

    val alpha3 by transition3.animateFloat(
        initialValue = 0.04f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha3"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                val gridSize = 50.dp.toPx()
                val gridColor = Color.White.copy(alpha = 0.02f)
                onDrawWithContent {
                    drawContent()
                    
                    // 绘制水平网格线
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += gridSize
                    }
                    
                    // 绘制垂直网格线
                    var x = 0f
                    while (x < size.width) {
                        drawLine(
                            color = gridColor,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f
                        )
                        x += gridSize
                    }
                }
            }
    ) {
        // 背景底色
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        )

        // 光晕1 - 左上角
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF06B6D4).copy(alpha = alpha1),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // 光晕2 - 右下角
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = 180.dp, y = 400.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF3B82F6).copy(alpha = alpha2),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // 光晕3 - 中间偏右上
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = 200.dp, y = 20.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6366F1).copy(alpha = alpha3),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}
