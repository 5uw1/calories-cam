package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.FiberColor
import com.example.ui.theme.ProteinColor

import com.example.util.AppLanguage
import com.example.util.AppStrings

@Composable
fun MacroItem(
    label: String,
    value: Float,
    unit: String = "g",
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(vertical = 12.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${value.toInt()}$unit",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun CalorieSummaryCard(
    consumedCalories: Int,
    goalCalories: Int,
    totalProtein: Float,
    totalCarbs: Float,
    totalFat: Float,
    language: AppLanguage = AppLanguage.EN,
    modifier: Modifier = Modifier
) {
    val progress = (consumedCalories.toFloat() / goalCalories.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "CalorieProgress")
    val remaining = (goalCalories - consumedCalories).coerceAtLeast(0)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Big Hero Calorie Progress Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(170.dp)
                    .padding(8.dp)
            ) {
                // Background Track
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(150.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
                    strokeWidth = 14.dp,
                    strokeCap = StrokeCap.Round
                )
                // Active Progress Arc
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(150.dp),
                    color = if (consumedCalories <= goalCalories) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    strokeWidth = 14.dp,
                    strokeCap = StrokeCap.Round
                )

                // Large numbers inside ring
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$consumedCalories",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 38.sp,
                            lineHeight = 42.sp
                        ),
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "/ $goalCalories kcal",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Minimal Remaining status
            Text(
                text = if (consumedCalories <= goalCalories) {
                    AppStrings.remainingCalories(remaining, language)
                } else {
                    AppStrings.exceededCalories(consumedCalories - goalCalories, language)
                },
                style = MaterialTheme.typography.titleSmall,
                color = if (consumedCalories <= goalCalories) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3 Clean Macro Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MacroItem(
                    label = AppStrings.protein(language),
                    value = totalProtein,
                    color = ProteinColor,
                    modifier = Modifier.weight(1f)
                )
                MacroItem(
                    label = AppStrings.carbs(language),
                    value = totalCarbs,
                    color = CarbsColor,
                    modifier = Modifier.weight(1f)
                )
                MacroItem(
                    label = AppStrings.fat(language),
                    value = totalFat,
                    color = FatColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
