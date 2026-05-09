package com.periodictable.ui.components

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.blur
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.periodictable.data.Element
import com.periodictable.data.categoryInfo

@Composable
fun ElementDetail(
    element: Element?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (element == null) return

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = true,
            dismissOnBackPress = true
        )
    ) {
        // 背景蒙层 + 垂直滚动容器 - 对齐web端的 overflow-y-auto 和 backdrop blur
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)) // bg-black/70 - 对齐web端透明度
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // 主卡片容器 - 对齐web端 max-w-2xl 和 animate-in 效果
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(200)) +
                        scaleIn(
                            animationSpec = tween(200),
                            initialScale = 0.95f
                        ),
                exit = fadeOut(animationSpec = tween(150)) +
                        scaleOut(
                            animationSpec = tween(150),
                            targetScale = 0.95f
                        )
            ) {
                Surface(
                    modifier = modifier
                        .fillMaxWidth()
                        .widthIn(max = 480.dp) // 适配移动端，约等于web端max-w-2xl的比例
                        .shadow(
                            elevation = 24.dp, // 对齐web端 shadow-2xl
                            spotColor = Color.Black.copy(alpha = 0.4f),
                            ambientColor = Color.Black.copy(alpha = 0.2f)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A).copy(alpha = 0.95f), // bg-slate-900/95
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f) // border-white/10
                    )
                ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val categoryData = categoryInfo[element.category]!!

                    // 顶部颜色条 - 对齐web端 h-2
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp) // h-2 = 8dp
                            .background(categoryData.color)
                    )

                    // 关闭按钮 - 对齐web端 absolute top-4 right-4
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, end = 16.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.1f)) // bg-white/10
                                .clickable { onClose() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp) // w-4 h-4 = 16dp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 480.dp)
                            .verticalScroll(rememberScrollState())
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    ) {
                        // 主信息区域 - 对齐web端 flex items-start gap-6
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // 元素符号卡片 - 对齐web端 w-24 h-24
                            Box(
                                modifier = Modifier
                                    .size(96.dp) // w-24 h-24 = 96dp
                                    .clip(RoundedCornerShape(8.dp)) // rounded-lg = 8dp
                                    .background(categoryData.color)
                                    .border(
                                        width = 1.dp,
                                        color = Color.White.copy(alpha = 0.2f), // border-white/20
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = element.atomicNumber.toString(),
                                        color = Color.White.copy(alpha = 0.7f), // text-white/70
                                        fontSize = 12.sp, // text-xs = 12sp
                                        fontWeight = FontWeight.Normal
                                    )
                                    Text(
                                        text = element.symbol,
                                        color = Color.White,
                                        fontSize = 36.sp, // text-4xl = 36sp
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = String.format("%.3f", element.atomicMass),
                                        color = Color.White.copy(alpha = 0.8f), // text-white/80
                                        fontSize = 14.sp // text-sm = 14sp
                                    )
                                }
                            }

                            // 名称和类别 - 对齐web端 flex-1
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = element.nameZh,
                                    color = Color.White,
                                    fontSize = 24.sp, // text-2xl = 24sp
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = element.nameEn,
                                    color = Color.White.copy(alpha = 0.7f), // text-white/70
                                    fontSize = 18.sp // text-lg = 18sp
                                )

                                Spacer(modifier = Modifier.height(8.dp)) // mt-2

                                // 类别标签 - 对齐web端 rounded-full px-3 py-1
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(categoryData.color)
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${categoryData.nameZh} / ${categoryData.nameEn}",
                                        color = Color.White,
                                        fontSize = 12.sp // text-xs = 12sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp)) // mt-4

                        // 描述文字 - 对齐web端 text-sm text-white/70 leading-relaxed
                        Text(
                            text = element.description,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp, // text-sm = 14sp
                            lineHeight = 20.sp // leading-relaxed
                        )

                        Spacer(modifier = Modifier.height(24.dp)) // mt-6

                        // 电子壳层可视化 - 对齐web端 p-4 bg-white/5 rounded-lg border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)) // rounded-lg
                                .background(Color.White.copy(alpha = 0.05f)) // bg-white/5
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.1f), // border-white/10
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp) // p-4
                        ) {
                            Column {
                                Text(
                                    text = "电子层分布 / Electron Shells",
                                    color = Color.White.copy(alpha = 0.8f), // text-white/80
                                    fontSize = 14.sp, // text-sm
                                    fontWeight = FontWeight.Medium, // font-medium
                                    modifier = Modifier
                                        .padding(bottom = 16.dp) // mb-4
                                        .align(Alignment.CenterHorizontally) // text-center
                                )
                                ElectronShell(element = element, scale = 1.5f)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp)) // mt-6

                        // 属性网格 - 对齐web端 grid-cols-2 gap-4
                        val properties = listOf(
                            Triple("电子排布", "Electron Config", element.electronConfiguration),
                            Triple("电负性", "Electronegativity", element.electronegativity?.let { "%.2f".format(it) } ?: "N/A"),
                            Triple("熔点", "Melting Point", element.meltingPoint?.let { "%.2f °C".format(it) } ?: "N/A"),
                            Triple("沸点", "Boiling Point", element.boilingPoint?.let { "%.2f °C".format(it) } ?: "N/A"),
                            Triple("密度", "Density", element.density?.let { "%.4f g/cm³".format(it) } ?: "N/A"),
                            Triple("发现年份", "Discovery", element.discoveryYear?.toString() ?: "古代"),
                            Triple("周期", "Period", element.period.toString()),
                            Triple("族", "Group", element.group?.toString() ?: "N/A")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) { // gap-4
                            properties.chunked(2).forEach { rowProperties ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp) // gap-4
                                ) {
                                    rowProperties.forEach { (labelZh, labelEn, value) ->
                                        PropertyItem(
                                            labelZh = labelZh,
                                            labelEn = labelEn,
                                            value = value,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowProperties.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
                } // AnimatedVisibility
            }
        }
    }
}

@Composable
private fun PropertyItem(
    labelZh: String,
    labelEn: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)) // rounded-lg
            .background(Color.White.copy(alpha = 0.05f)) // bg-white/5
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.05f), // border-white/5
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp) // p-3
    ) {
        Column {
            Row {
                Text(
                    text = labelZh,
                    color = Color.White.copy(alpha = 0.5f), // text-white/50
                    fontSize = 12.sp // text-xs = 12sp
                )
                Text(
                    text = " / $labelEn",
                    color = Color.White.copy(alpha = 0.3f), // text-white/30
                    fontSize = 12.sp // text-xs = 12sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp)) // mb-1
            Text(
                text = value,
                color = Color.White,
                fontSize = 14.sp, // text-sm
                fontWeight = FontWeight.Medium, // font-medium
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}
