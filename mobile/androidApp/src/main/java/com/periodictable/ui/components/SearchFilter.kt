package com.periodictable.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.periodictable.data.ElementCategory
import com.periodictable.data.categoryInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategories: Set<ElementCategory>,
    onCategoriesChange: (Set<ElementCategory>) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFilterExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // 搜索行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 搜索框
            Box(modifier = Modifier.weight(1f)) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "搜索元素 (符号、名称、原子序数)...",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchChange("") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.08f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF06B6D4)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // 筛选按钮
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isFilterExpanded || selectedCategories.isNotEmpty())
                            Color(0xFF06B6D4).copy(alpha = 0.2f)
                        else
                            Color.White.copy(alpha = 0.08f)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isFilterExpanded || selectedCategories.isNotEmpty())
                            Color(0xFF06B6D4)
                        else
                            Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { isFilterExpanded = !isFilterExpanded },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "◐",
                        color = if (isFilterExpanded || selectedCategories.isNotEmpty())
                            Color(0xFF06B6D4) else Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (selectedCategories.isNotEmpty()) {
                        Text(
                            text = selectedCategories.size.toString(),
                            color = Color(0xFF06B6D4),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        // 可展开的筛选面板
        AnimatedVisibility(
            visible = isFilterExpanded,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                // 标题行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "按类别筛选",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    if (selectedCategories.isNotEmpty()) {
                        Text(
                            text = "清除筛选",
                            color = Color(0xFF06B6D4),
                            fontSize = 11.sp,
                            modifier = Modifier.clickable {
                                onCategoriesChange(emptySet())
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 类别按钮网格
                val categories = ElementCategory.values().toList()
                val rows = categories.chunked(2)

                rows.forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowCategories.forEach { category ->
                            val categoryData = categoryInfo[category]!!
                            val isSelected = selectedCategories.contains(category)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) categoryData.color.copy(alpha = 0.3f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) categoryData.color
                                        else Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        val newSet = if (isSelected) {
                                            selectedCategories - category
                                        } else {
                                            selectedCategories + category
                                        }
                                        onCategoriesChange(newSet)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(categoryData.color)
                                    )
                                    Text(
                                        text = categoryData.nameZh,
                                        color = if (isSelected) Color.White
                                        else Color.White.copy(alpha = 0.7f),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        if (rowCategories.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}
