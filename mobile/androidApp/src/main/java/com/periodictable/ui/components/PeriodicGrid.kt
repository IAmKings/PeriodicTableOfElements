package com.periodictable.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.ElementCategory
import com.periodictable.data.categoryInfo
import com.periodictable.data.elements
import com.periodictable.data.getElementPosition

@Composable
fun PeriodicGrid(
    selectedCategories: Set<ElementCategory>,
    searchQuery: String,
    onElementClick: (Element) -> Unit,
    onCategoriesChange: (Set<ElementCategory>) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainElements = elements.filter { 
        it.category != ElementCategory.LANTHANIDE && it.category != ElementCategory.ACTINIDE 
    }
    val lanthanides = elements.filter { it.category == ElementCategory.LANTHANIDE }
    val actinides = elements.filter { it.category == ElementCategory.ACTINIDE }

    val grid = Array(10) { Array<Element?>(18) { null } }
    
    mainElements.forEach { element ->
        val pos = getElementPosition(element)
        if (pos != null && pos.first < 7) {
            grid[pos.first][pos.second] = element
        }
    }

    lanthanides.forEachIndexed { index, element ->
        grid[7][3 + index] = element
    }
    
    actinides.forEachIndexed { index, element ->
        grid[8][3 + index] = element
    }

    fun isElementHighlighted(element: Element): Boolean {
        if (selectedCategories.isNotEmpty() && !selectedCategories.contains(element.category)) {
            return false
        }
        if (searchQuery.isNotEmpty()) {
            val q = searchQuery.lowercase()
            return element.symbol.lowercase().contains(q) ||
                   element.nameEn.lowercase().contains(q) ||
                   element.nameZh.contains(q) ||
                   element.atomicNumber.toString() == q
        }
        return true
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 24.dp)
        ) {
            for (i in 1..18) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = i.toString(),
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            for (row in 0..6) {
                Row(modifier = Modifier.padding(vertical = 1.dp)) {
                    Box(
                        modifier = Modifier.size(24.dp, 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (row + 1).toString(),
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 10.sp
                        )
                    }
                    for (col in 0..17) {
                        val element = grid[row][col]
                        if (element != null) {
                            ElementCard(
                                element = element,
                                isHighlighted = isElementHighlighted(element),
                                onClick = onElementClick
                            )
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            for (row in 7..8) {
                Row(modifier = Modifier.padding(vertical = 1.dp)) {
                    Box(
                        modifier = Modifier.size(24.dp, 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (row == 7) "*" else "**",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(48.dp * 3))
                    for (col in 3..17) {
                        val element = grid[row][col]
                        if (element != null) {
                            ElementCard(
                                element = element,
                                isHighlighted = isElementHighlighted(element),
                                onClick = onElementClick
                            )
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CategoryLegend(
            selectedCategories = selectedCategories,
            onCategoriesChange = onCategoriesChange
        )
    }
}

@Composable
fun CategoryLegend(
    selectedCategories: Set<ElementCategory>,
    onCategoriesChange: (Set<ElementCategory>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "元素分类",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        
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
                                color = if (isSelected) categoryData.color else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                val newSet = if (isSelected) {
                                    selectedCategories - category
                                } else {
                                    selectedCategories + category
                                }
                                onCategoriesChange(newSet)
                            }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(8.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(categoryData.color)
                            )
                            Text(
                                text = categoryData.nameZh,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
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
