package com.periodictable.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.ElementCategory
import com.periodictable.ui.components.DataVisualization
import com.periodictable.ui.components.ElementDetail
import com.periodictable.ui.components.PeriodicGrid
import com.periodictable.ui.components.QuizMode
import com.periodictable.ui.components.SearchFilter
import com.periodictable.ui.theme.AnimatedBackground

enum class ViewMode {
    TABLE, QUIZ, VISUALIZATION
}

@Composable
fun PeriodicTableScreen(
    modifier: Modifier = Modifier
) {
    var selectedElement by remember { mutableStateOf<Element?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf<Set<ElementCategory>>(emptySet()) }
    var viewMode by remember { mutableStateOf(ViewMode.TABLE) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedBackground()

        Column {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF06B6D4),
                                            Color(0xFF3B82F6)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "H",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                text = "元素周期表",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Periodic Table of Elements",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // View Mode Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TabButton(
                            text = "📊 周期表",
                            isSelected = viewMode == ViewMode.TABLE,
                            onClick = { viewMode = ViewMode.TABLE },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            text = "🎯 测验",
                            isSelected = viewMode == ViewMode.QUIZ,
                            onClick = { viewMode = ViewMode.QUIZ },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            text = "📈 可视化",
                            isSelected = viewMode == ViewMode.VISUALIZATION,
                            onClick = { viewMode = ViewMode.VISUALIZATION },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (viewMode == ViewMode.TABLE) {
                        SearchFilter(
                            searchQuery = searchQuery,
                            onSearchChange = { searchQuery = it },
                            selectedCategories = selectedCategories,
                            onCategoriesChange = { selectedCategories = it }
                        )
                    }
                }
            }

            when (viewMode) {
                ViewMode.TABLE -> {
                    PeriodicGrid(
                        selectedCategories = selectedCategories,
                        searchQuery = searchQuery,
                        onElementClick = { element ->
                            selectedElement = element
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                ViewMode.QUIZ -> {
                    QuizMode(
                        modifier = Modifier.weight(1f)
                    )
                }
                ViewMode.VISUALIZATION -> {
                    DataVisualization(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        ElementDetail(
            element = selectedElement,
            onClose = { selectedElement = null }
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.2f)
                else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.5f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF06B6D4)
            else Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

