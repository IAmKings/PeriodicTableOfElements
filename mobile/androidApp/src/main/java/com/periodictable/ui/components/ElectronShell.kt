package com.periodictable.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.ElectronShells
import com.periodictable.data.categoryInfo
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ElectronShell(
    element: Element,
    modifier: Modifier = Modifier
) {
    val shells = ElectronShells.getElectronShells(element.atomicNumber)
    val category = categoryInfo[element.category]!!
    val primaryColor = category.color

    var mounted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mounted = true
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.03f))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            ElectronShellCanvas(
                element = element,
                shells = shells,
                primaryColor = primaryColor,
                mounted = mounted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 电子壳层信息
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            shells.forEachIndexed { index, electronCount ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = ElectronShells.shellNames[index],
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = electronCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 总电子数
        Text(
            text = "总电子数: ${element.atomicNumber}",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ElectronShellCanvas(
    element: Element,
    shells: List<Int>,
    primaryColor: Color,
    mounted: Boolean
) {
    val baseRadius = 24f
    val radiusIncrement = 18f
    val centerX = 120f
    val centerY = 120f

    // 轨道动画旋转 - 每层不同方向和速度
    val rotations = shells.mapIndexed { index, _ ->
        val transition = rememberInfiniteTransition(label = "orbit-$index")
        val duration = 3000 + index * 1500
        val direction = if (index % 2 == 0) 1f else -1f
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f * direction,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation-$index"
        ).value
    }

    // 脉冲动画
    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha = pulseTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse-alpha"
    ).value

    Canvas(modifier = Modifier.fillMaxSize()) {
        // 背景光晕
        val glowRadius = baseRadius + shells.size * radiusIncrement + 30f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = pulseAlpha * 0.5f),
                    primaryColor.copy(alpha = 0f)
                ),
                center = Offset(centerX, centerY),
                radius = glowRadius
            ),
            radius = glowRadius,
            center = Offset(centerX, centerY)
        )

        // 电子壳层轨道
        shells.forEachIndexed { index, _ ->
            val orbitRadius = baseRadius + index * radiusIncrement
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = orbitRadius,
                center = Offset(centerX, centerY),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 1f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                        floatArrayOf(4f, 4f),
                        phase = if (mounted) (index * 10).toFloat() else 0f
                    )
                )
            )
        }

        // 原子核光晕
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.4f),
                    primaryColor.copy(alpha = 0f)
                ),
                center = Offset(centerX, centerY),
                radius = 24f
            ),
            radius = 24f,
            center = Offset(centerX, centerY)
        )

        // 原子核
        drawCircle(
            color = primaryColor,
            radius = 16f,
            center = Offset(centerX, centerY)
        )

        // 电子 - 每层旋转
        shells.forEachIndexed { shellIndex, electronCount ->
            val orbitRadius = baseRadius + shellIndex * radiusIncrement
            val angleStep = 2 * Math.PI.toFloat() / electronCount
            val rotation = rotations[shellIndex]

            rotate(degrees = rotation, pivot = Offset(centerX, centerY)) {
                for (i in 0 until electronCount) {
                    val angle = angleStep * i - Math.PI.toFloat() / 2
                    val electronX = centerX + orbitRadius * cos(angle)
                    val electronY = centerY + orbitRadius * sin(angle)

                    // 电子光晕
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.3f),
                        radius = 6f,
                        center = Offset(electronX, electronY)
                    )

                    // 电子核心
                    drawCircle(
                        color = primaryColor,
                        radius = 3f,
                        center = Offset(electronX, electronY)
                    )

                    // 电子高光
                    drawCircle(
                        color = Color.White.copy(alpha = 0.8f),
                        radius = 1f,
                        center = Offset(electronX - 1f, electronY - 1f)
                    )
                }
            }
        }
    }

    // 绘制元素符号（在 Canvas 上层）
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = element.symbol,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
