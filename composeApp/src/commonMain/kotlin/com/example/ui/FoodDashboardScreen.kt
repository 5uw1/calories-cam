package com.example.ui

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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodEntry
import com.example.platform.ImageCodec
import com.example.platform.decodeImageBitmap
import com.example.ui.components.CalorieSummaryCard
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.util.DateFormat
import com.example.util.ThemeMode

@Composable
fun FoodDashboardScreen(
    entries: List<FoodEntry>,
    goalCalories: Int,
    language: AppLanguage = AppLanguage.EN,
    isDarkMode: Boolean = false,
    onToggleLanguage: () -> Unit = {},
    onToggleTheme: () -> Unit = {},
    onOpenScanner: () -> Unit,
    onOpenHistory: () -> Unit = {},
    onSelectEntry: (FoodEntry) -> Unit,
    onDeleteEntry: (FoodEntry) -> Unit,
    onUpdateGoal: (Int) -> Unit
) {
    val todayDateString = remember(language) {
        DateFormat.fullDate(DateFormat.nowMillis(), language == AppLanguage.TH)
    }

    var showGoalDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    // Filter today's entries
    val todayEntries = remember(entries) {
        entries.filter { entry -> DateFormat.isToday(entry.timestamp) }
    }

    val totalConsumedCalories = todayEntries.sumOf { it.calories }
    val totalProtein = todayEntries.sumOf { it.protein.toDouble() }.toFloat()
    val totalCarbs = todayEntries.sumOf { it.carbs.toDouble() }.toFloat()
    val totalFat = todayEntries.sumOf { it.fat.toDouble() }.toFloat()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with App Title and Menu
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.appTitle(language),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontSize = 32.sp,
                                lineHeight = 36.sp
                            ),
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = todayDateString,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Single Overflow Menu button containing History, Settings, Language
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier
                                .size(44.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            // History
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        AppStrings.menuHistory(language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                onClick = {
                                    showMenu = false
                                    onOpenHistory()
                                }
                            )

                            // Daily Goal Settings
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        AppStrings.menuSettings(language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Tune, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                onClick = {
                                    showMenu = false
                                    showGoalDialog = true
                                }
                            )

                            // Switch Language
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        AppStrings.menuLanguage(language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                onClick = {
                                    showMenu = false
                                    onToggleLanguage()
                                }
                            )

                            // Toggle Dark / Light Theme
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        AppStrings.menuTheme(isDarkMode, language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onToggleTheme()
                                }
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
            if (todayEntries.isNotEmpty()) {
                item {
                    Text(
                        text = AppStrings.todayFoodLog(todayEntries.size, language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                    )
                }
            }

            if (todayEntries.isEmpty()) {
                item {
                    EmptyFoodLogCard(language = language)
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

        // Prominent Floating Action Button (FAB) for Scan Food (Icon only - Large primary action)
        FloatingActionButton(
            onClick = onOpenScanner,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 12.dp),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .size(76.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = AppStrings.navScan(language),
                modifier = Modifier.size(38.dp)
            )
        }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = mealType,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "$totalCalories kcal",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
    val imgBitmap = remember(entry.imageBase64) {
        if (!entry.imageBase64.isNullOrBlank()) {
            try {
                decodeImageBitmap(ImageCodec.decodeFromBase64(entry.imageBase64))
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
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Thumbnail
            if (imgBitmap != null) {
                Image(
                    bitmap = imgBitmap,
                    contentDescription = displayName,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

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
            }

            // Calories badge
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "${entry.calories}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp
                    ),
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = AppStrings.deleteItem(language),
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyFoodLogCard(
    language: AppLanguage = AppLanguage.EN
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = AppStrings.emptyLogTitle(language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppStrings.emptyLogSubtitle(language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

