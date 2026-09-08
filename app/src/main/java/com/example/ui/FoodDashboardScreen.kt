package com.example.ui

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodEntry
import com.example.ui.components.CalorieSummaryCard
import com.example.util.AppLanguage
import com.example.util.AppStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FoodDashboardScreen(
    entries: List<FoodEntry>,
    goalCalories: Int,
    language: AppLanguage = AppLanguage.EN,
    onToggleLanguage: () -> Unit = {},
    onOpenScanner: () -> Unit,
    onSelectEntry: (FoodEntry) -> Unit,
    onDeleteEntry: (FoodEntry) -> Unit,
    onUpdateGoal: (Int) -> Unit
) {
    val todayDateFormat = remember(language) {
        if (language == AppLanguage.TH) {
            SimpleDateFormat("EEEEที่ d MMMM yyyy", Locale("th", "TH"))
        } else {
            SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
        }
    }
    val todayDateString = remember(language) { todayDateFormat.format(Date()) }

    var showGoalDialog by remember { mutableStateOf(false) }

    // Filter today's entries
    val todayEntries = remember(entries) {
        val calToday = java.util.Calendar.getInstance()
        entries.filter { entry ->
            val calEntry = java.util.Calendar.getInstance().apply { timeInMillis = entry.timestamp }
            calToday.get(java.util.Calendar.YEAR) == calEntry.get(java.util.Calendar.YEAR) &&
            calToday.get(java.util.Calendar.DAY_OF_YEAR) == calEntry.get(java.util.Calendar.DAY_OF_YEAR)
        }
    }

    val totalConsumedCalories = todayEntries.sumOf { it.calories }
    val totalProtein = todayEntries.sumOf { it.protein.toDouble() }.toFloat()
    val totalCarbs = todayEntries.sumOf { it.carbs.toDouble() }.toFloat()
    val totalFat = todayEntries.sumOf { it.fat.toDouble() }.toFloat()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.appTitle(language),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = todayDateString,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Language Switcher Toggle Pill
                        Surface(
                            onClick = onToggleLanguage,
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            tonalElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.EN) "🇺🇸 EN" else "🇹🇭 TH",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        // Calorie target settings button
                        IconButton(
                            onClick = { showGoalDialog = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = AppStrings.setGoalTitle(language),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Daily Calorie Summary Card
            item {
                CalorieSummaryCard(
                    consumedCalories = totalConsumedCalories,
                    goalCalories = goalCalories,
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    totalFat = totalFat,
                    language = language
                )
            }

            // Meals header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.todayFoodLog(todayEntries.size, language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            if (todayEntries.isEmpty()) {
                item {
                    EmptyFoodLogCard(
                        language = language,
                        onOpenScanner = onOpenScanner
                    )
                }
            } else {
                // Group by Meal Type
                val standardMeals = listOf("Breakfast", "Lunch", "Dinner", "Snack", "มื้อเช้า", "มื้อเที่ยง", "มื้อเย็น", "ของว่าง")
                val groupedMeals = todayEntries.groupBy { AppStrings.translateMeal(it.mealType, language) }

                val currentLangMealOrder = AppStrings.mealList(language)

                currentLangMealOrder.forEach { displayMealType ->
                    val mealItems = groupedMeals[displayMealType]
                    if (!mealItems.isNullOrEmpty()) {
                        item {
                            MealSectionHeader(
                                mealType = displayMealType,
                                totalCalories = mealItems.sumOf { it.calories }
                            )
                        }
                        items(mealItems, key = { it.id }) { food ->
                            FoodLogItemCard(
                                entry = food,
                                language = language,
                                onClick = { onSelectEntry(food) },
                                onDelete = { onDeleteEntry(food) }
                            )
                        }
                    }
                }

                // Any other meal type
                groupedMeals.filterKeys { it !in currentLangMealOrder }.forEach { (mealType, mealItems) ->
                    item {
                        MealSectionHeader(
                            mealType = mealType,
                            totalCalories = mealItems.sumOf { it.calories }
                        )
                    }
                    items(mealItems, key = { it.id }) { food ->
                        FoodLogItemCard(
                            entry = food,
                            language = language,
                            onClick = { onSelectEntry(food) },
                            onDelete = { onDeleteEntry(food) }
                        )
                    }
                }
            }
        }

        // Floating Action Button to scan food
        ExtendedFloatingActionButton(
            onClick = onOpenScanner,
            icon = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
            text = { Text(AppStrings.openScanner(language), fontWeight = FontWeight.Bold) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }

    // Goal adjustment dialog
    if (showGoalDialog) {
        var goalInput by remember { mutableStateOf(goalCalories.toString()) }
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text(AppStrings.setGoalTitle(language)) },
            text = {
                Column {
                    Text(
                        AppStrings.setGoalDesc(language),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = goalInput,
                        onValueChange = { goalInput = it.filter { ch -> ch.isDigit() } },
                        label = { Text(AppStrings.targetCalorie(language)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = goalInput.toIntOrNull()
                        if (parsed != null && parsed >= 500) {
                            onUpdateGoal(parsed)
                        }
                        showGoalDialog = false
                    }
                ) {
                    Text(AppStrings.save(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text(AppStrings.cancel(language))
                }
            }
        )
    }
}

@Composable
fun MealSectionHeader(mealType: String, totalCalories: Int) {
    val icon = when {
        mealType.contains("Breakfast", ignoreCase = true) || mealType.contains("เช้า") -> Icons.Default.WbSunny
        mealType.contains("Lunch", ignoreCase = true) || mealType.contains("เที่ยง") -> Icons.Default.LunchDining
        mealType.contains("Dinner", ignoreCase = true) || mealType.contains("เย็น") -> Icons.Default.Nightlight
        else -> Icons.Default.Fastfood
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = mealType,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = "$totalCalories kcal",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FoodLogItemCard(
    entry: FoodEntry,
    language: AppLanguage = AppLanguage.EN,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val bitmap = remember(entry.imageBase64) {
        if (!entry.imageBase64.isNullOrBlank()) {
            try {
                val bytes = Base64.decode(entry.imageBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    val displayName = if (language == AppLanguage.EN && entry.foodNameEn.isNotBlank()) {
        entry.foodNameEn
    } else {
        entry.foodName
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Thumbnail
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = displayName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Food info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = entry.portionSize,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "P: ${entry.protein.toInt()}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "C: ${entry.carbs.toInt()}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "F: ${entry.fat.toInt()}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Calories badge
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.calories}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = AppStrings.deleteItem(language),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyFoodLogCard(
    language: AppLanguage = AppLanguage.EN,
    onOpenScanner: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = AppStrings.emptyLogTitle(language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = AppStrings.emptyLogSubtitle(language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onOpenScanner,
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.openScanner(language))
            }
        }
    }
}

