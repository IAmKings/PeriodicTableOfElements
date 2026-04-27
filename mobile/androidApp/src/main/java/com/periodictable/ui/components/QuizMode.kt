package com.periodictable.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.periodictable.data.Element
import com.periodictable.data.ElementCategory
import com.periodictable.data.categoryInfo
import com.periodictable.data.elements
import kotlin.random.Random

enum class QuizType {
    SYMBOL, NAME, CATEGORY, ATOMIC_NUMBER
}

data class Question(
    val element: Element,
    val type: QuizType,
    val options: List<String>,
    val correctAnswer: String
)

@Composable
fun QuizMode(
    modifier: Modifier = Modifier
) {
    var currentQuestion by remember { mutableStateOf<Question?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var totalQuestions by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var streak by remember { mutableIntStateOf(0) }

    fun generateQuestion(): Question {
        val element = elements.random()
        val types = QuizType.values()
        val type = types.random()

        val options: MutableList<String> = mutableListOf()
        val correctAnswer: String

        when (type) {
            QuizType.SYMBOL -> {
                correctAnswer = element.symbol
                options.add(element.symbol)
                while (options.size < 4) {
                    val randomElement = elements.random()
                    if (randomElement.symbol !in options) {
                        options.add(randomElement.symbol)
                    }
                }
            }
            QuizType.NAME -> {
                correctAnswer = element.nameZh
                options.add(element.nameZh)
                while (options.size < 4) {
                    val randomElement = elements.random()
                    if (randomElement.nameZh !in options) {
                        options.add(randomElement.nameZh)
                    }
                }
            }
            QuizType.CATEGORY -> {
                correctAnswer = categoryInfo[element.category]!!.nameZh
                options.addAll(ElementCategory.values().map { categoryInfo[it]!!.nameZh }.shuffled().take(4))
                if (correctAnswer !in options) {
                    options[0] = correctAnswer
                }
            }
            QuizType.ATOMIC_NUMBER -> {
                correctAnswer = element.atomicNumber.toString()
                options.add(correctAnswer)
                while (options.size < 4) {
                    val randomNum = Random.nextInt(1, 119).toString()
                    if (randomNum !in options) {
                        options.add(randomNum)
                    }
                }
            }
        }

        return Question(
            element = element,
            type = type,
            options = options.shuffled(),
            correctAnswer = correctAnswer
        )
    }

    LaunchedEffect(Unit) {
        currentQuestion = generateQuestion()
    }

    fun handleAnswer(answer: String) {
        if (showResult) return
        selectedAnswer = answer
        showResult = true
        totalQuestions++
        if (answer == currentQuestion?.correctAnswer) {
            score++
            streak++
        } else {
            streak = 0
        }
    }

    fun nextQuestion() {
        selectedAnswer = null
        showResult = false
        currentQuestion = generateQuestion()
    }

    fun resetQuiz() {
        score = 0
        totalQuestions = 0
        streak = 0
        selectedAnswer = null
        showResult = false
        currentQuestion = generateQuestion()
    }

    val question = currentQuestion ?: return

    val categoryData = categoryInfo[question.element.category]!!

    val questionText = when (question.type) {
        QuizType.SYMBOL -> "\"${question.element.nameZh}\" 的元素符号是什么？"
        QuizType.NAME -> "元素符号 \"${question.element.symbol}\" 代表哪个元素？"
        QuizType.CATEGORY -> "\"${question.element.nameZh} (${question.element.symbol})\" 属于哪一类？"
        QuizType.ATOMIC_NUMBER -> "\"${question.element.nameZh} (${question.element.symbol})\" 的原子序数是多少？"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部状态栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // 分数
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEAB308).copy(alpha = 0.2f))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFEAB308).copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "🏆",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$score / $totalQuestions",
                            color = Color(0xFFEAB308),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // 连击
                AnimatedVisibility(visible = streak > 1) {
                    val pulseScale by animateFloatAsState(
                        targetValue = if (streak > 1) 1.05f else 1f,
                        animationSpec = tween(durationMillis = 500),
                        label = "streak-pulse"
                    )
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF97316).copy(alpha = 0.2f))
                            .border(
                                width = 1.dp,
                                color = Color(0xFFF97316).copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "⚡",
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$streak 连击!",
                                color = Color(0xFFF97316),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // 重新开始按钮
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { resetQuiz() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "🔄",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "重新开始",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 元素显示卡片
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(categoryData.color)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = question.element.atomicNumber.toString(),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = question.element.symbol,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = question.element.nameZh,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 问题文字
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎯",
                    fontSize = 14.sp
                )
                Text(
                    text = "问题",
                    color = Color(0xFF06B6D4).copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = questionText,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 选项网格
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            question.options.chunked(2).forEach { rowOptions ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowOptions.forEach { option ->
                        val isCorrect = option == question.correctAnswer
                        val isSelected = option == selectedAnswer

                        val backgroundColor = if (showResult) {
                            when {
                                isCorrect -> Color(0xFF22C55E).copy(alpha = 0.2f)
                                isSelected -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                else -> Color.White.copy(alpha = 0.05f)
                            }
                        } else {
                            Color.White.copy(alpha = 0.05f)
                        }

                        val borderColor = if (showResult) {
                            when {
                                isCorrect -> Color(0xFF22C55E).copy(alpha = 0.5f)
                                isSelected -> Color(0xFFEF4444).copy(alpha = 0.5f)
                                else -> Color.White.copy(alpha = 0.1f)
                            }
                        } else {
                            Color.White.copy(alpha = 0.1f)
                        }

                        val textColor = if (showResult) {
                            when {
                                isCorrect -> Color(0xFF22C55E)
                                isSelected -> Color(0xFFEF4444)
                                else -> Color.White.copy(alpha = 0.5f)
                            }
                        } else {
                            Color.White
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(backgroundColor)
                                .border(
                                    width = 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = !showResult) {
                                    handleAnswer(option)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (showResult && isCorrect) {
                                    Text(text = "✓", fontSize = 16.sp, color = Color(0xFF22C55E))
                                }
                                if (showResult && isSelected && !isCorrect) {
                                    Text(text = "✗", fontSize = 16.sp, color = Color(0xFFEF4444))
                                }
                                Text(
                                    text = option,
                                    color = textColor,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    if (rowOptions.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // 结果反馈
        AnimatedVisibility(visible = showResult) {
            Column(
                modifier = Modifier.padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isCorrect = selectedAnswer == question.correctAnswer
                val feedbackColor = if (isCorrect) Color(0xFF22C55E) else Color(0xFFEF4444)
                val feedbackBgColor = feedbackColor.copy(alpha = 0.2f)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(feedbackBgColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isCorrect) "✓" else "✗",
                            color = feedbackColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isCorrect) "正确!" else "错误! 正确答案是: ${question.correctAnswer}",
                            color = feedbackColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 下一题按钮
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF06B6D4).copy(alpha = 0.2f))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF06B6D4).copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { nextQuestion() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "下一题",
                        color = Color(0xFF06B6D4),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
