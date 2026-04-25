package com.periodictable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ElementCard(
    element: Element,
    isHighlighted: Boolean,
    onClick: (Element) -> Unit,
    modifier: Modifier = Modifier,
    cellSize: Int = 48
) {
    val categoryData = categoryInfo[element.category]!!

    Box(
        modifier = modifier
            .size(cellSize.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (isHighlighted) categoryData.color else categoryData.color.copy(alpha = 0.85f)
            )
            .border(
                width = 1.dp,
                color = if (isHighlighted) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick(element) }
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
