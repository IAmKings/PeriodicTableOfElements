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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
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
                        .widthIn(max = 480.dp)
                        .shadow(
                            elevation = 24.dp,
                            spotColor = Color.Black.copy(alpha = 0.4f),
                            ambientColor = Color.Black.copy(alpha = 0.2f)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A).copy(alpha = 0.95f),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val categoryData = categoryInfo[element.category]!!
                        val scrollState = rememberScrollState()
                        var showHint by remember { mutableStateOf(true) }
                        val density = LocalDensity.current
                        val coroutineScope = rememberCoroutineScope()

                        LaunchedEffect(scrollState.value) {
                            val threshold = with(density) { 5.dp.toPx() }.toInt()
                            val isAtBottom = scrollState.value >= scrollState.maxValue - threshold
                            if (isAtBottom) {
                                delay(100)
                                showHint = false
                            } else {
                                showHint = true
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(categoryData.color)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                            )
                        }

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
                                    .background(Color.White.copy(alpha = 0.1f))
                                    .clickable { onClose() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 480.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)
                                    .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(96.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(categoryData.color)
                                            .border(
                                                width = 1.dp,
                                                color = Color.White.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Text(
                                                text = element.atomicNumber.toString(),
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                            Text(
                                                text = element.symbol,
                                                color = Color.White,
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = String.format("%.3f", element.atomicMass),
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 14.sp
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = element.nameZh,
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = element.nameEn,
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(categoryData.color)
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "${categoryData.nameZh} / ${categoryData.nameEn}",
                                                color = Color.White,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = element.description,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.05f))
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "电子层分布 / Electron Shells",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(bottom = 16.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )
                                        ElectronShell(element = element, scale = 1.5f)
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

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

                                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    properties.chunked(2).forEach { rowProperties ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
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

                            androidx.compose.animation.AnimatedVisibility(
                                visible = showHint,
                                enter = fadeIn(animationSpec = tween(300)),
                                exit = fadeOut(animationSpec = tween(300)),
                                modifier = Modifier.align(Alignment.BottomCenter)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color(0xFF0F172A).copy(alpha = 0.9f)
                                                )
                                            )
                                        )
                                        .clickable {
                                            coroutineScope.launch {
                                                scrollState.animateScrollTo(scrollState.maxValue)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Swipe down",
                                            tint = Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "下滑查看更多",
                                            color = Color.White.copy(alpha = 0.5f),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyItem(
    labelZh: String,
    labelEn: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val labelText = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)) {
            append(labelZh)
        }
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.3f), fontSize = 12.sp)) {
            append(" / $labelEn")
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Text(text = labelText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}
