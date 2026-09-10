package com.example.util

object AppStrings {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "CalorieCam"
        AppLanguage.TH -> "CalorieCam"
    }

    // Menu options
    fun menuSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Daily Goal Setting"
        AppLanguage.TH -> "ตั้งเป้าหมายแคลอรี่"
    }

    fun menuHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "History"
        AppLanguage.TH -> "ประวัติอาหาร"
    }

    fun menuLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "ภาษาไทย (TH)"
        AppLanguage.TH -> "English (EN)"
    }

    fun menuTheme(isDark: Boolean, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> if (isDark) "Light Mode" else "Dark Mode"
        AppLanguage.TH -> if (isDark) "โหมดสว่าง" else "โหมดมืด"
    }

    // Navigation
    fun navToday(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Today"
        AppLanguage.TH -> "วันนี้"
    }

    fun navScan(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan Food"
        AppLanguage.TH -> "สแกนอาหาร"
    }

    fun navHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "History"
        AppLanguage.TH -> "ประวัติ"
    }

    // Dashboard
    fun dailyEnergy(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Today's Intake"
        AppLanguage.TH -> "พลังงานวันนี้"
    }

    fun remainingCalories(remaining: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "$remaining kcal remaining"
        AppLanguage.TH -> "เหลือได้อีก $remaining kcal"
    }

    fun exceededCalories(exceeded: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "$exceeded kcal over goal"
        AppLanguage.TH -> "เกินเป้าหมาย $exceeded kcal"
    }

    fun protein(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Protein"
        AppLanguage.TH -> "โปรตีน"
    }

    fun carbs(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Carbs"
        AppLanguage.TH -> "คาร์บ"
    }

    fun fat(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Fat"
        AppLanguage.TH -> "ไขมัน"
    }

    fun fiber(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Fiber"
        AppLanguage.TH -> "ใยอาหาร"
    }

    fun sugar(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Sugar"
        AppLanguage.TH -> "น้ำตาล"
    }

    fun sodium(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Sodium"
        AppLanguage.TH -> "โซเดียม"
    }

    fun todayFoodLog(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Today's Meals ($count items)"
        AppLanguage.TH -> "รายการอาหารวันนี้ ($count รายการ)"
    }

    fun emptyLogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "No meals logged today"
        AppLanguage.TH -> "ยังไม่มีบันทึกอาหารวันนี้"
    }

    fun emptyLogSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Take a photo of your food or select a dish, and AI will estimate calories & nutrients automatically."
        AppLanguage.TH -> "ใช้กล้องถ่ายรูปอาหารของคุณ แล้ว AI จะช่วยคำนวณสารอาหารและแคลอรี่ให้โดยอัตโนมัติ"
    }

    fun openScanner(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan Meal with Camera"
        AppLanguage.TH -> "เปิดกล้องถ่ายรูปอาหาร"
    }

    fun setGoalTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Set Daily Calorie Target"
        AppLanguage.TH -> "ปรับเป้าหมายแคลอรี่ประจำวัน"
    }

    fun setGoalDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Enter your target calories per day (kcal):"
        AppLanguage.TH -> "กำหนดปริมาณแคลอรี่ที่คุณต้องการต่อวัน (kcal):"
    }

    fun targetCalorie(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Target (kcal)"
        AppLanguage.TH -> "เป้าหมาย (kcal)"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Save"
        AppLanguage.TH -> "บันทึก"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Cancel"
        AppLanguage.TH -> "ยกเลิก"
    }

    fun close(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Close"
        AppLanguage.TH -> "ปิด"
    }

    fun energy(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Energy"
        AppLanguage.TH -> "พลังงาน"
    }

    // Meal types
    fun mealBreakfast(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Breakfast"
        AppLanguage.TH -> "มื้อเช้า"
    }

    fun mealLunch(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Lunch"
        AppLanguage.TH -> "มื้อเที่ยง"
    }

    fun mealDinner(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Dinner"
        AppLanguage.TH -> "มื้อเย็น"
    }

    fun mealSnack(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Snack"
        AppLanguage.TH -> "ของว่าง"
    }

    fun mealList(lang: AppLanguage): List<String> = listOf(
        mealBreakfast(lang),
        mealLunch(lang),
        mealDinner(lang),
        mealSnack(lang)
    )

    fun translateMeal(meal: String, lang: AppLanguage): String {
        return when (meal.trim().lowercase()) {
            "มื้อเช้า", "breakfast" -> mealBreakfast(lang)
            "มื้อเที่ยง", "lunch" -> mealLunch(lang)
            "มื้อเย็น", "dinner" -> mealDinner(lang)
            "ของว่าง", "snack" -> mealSnack(lang)
            else -> meal
        }
    }

    // Camera Screen
    fun scanInstruction(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Snap food to calculate calories"
        AppLanguage.TH -> "ถ่ายรูปอาหารเพื่อคำนวณแคลอรี่"
    }

    fun reticleGuide(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Align food within the frame"
        AppLanguage.TH -> "จัดให้อาหารอยู่ภายในกรอบ"
    }

    fun popularSamples(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "💡 Quick Presets (Tap to scan instantly):"
        AppLanguage.TH -> "💡 เมนูแนะนำ (แตะเพื่อสแกนทันที):"
    }

    fun tabSwiss(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇨🇭 Swiss"
        AppLanguage.TH -> "🇨🇭 สวิส"
    }

    fun tabItalian(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇮🇹 Italian"
        AppLanguage.TH -> "🇮🇹 อิตาเลียน"
    }

    fun tabFrench(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇫🇷 French"
        AppLanguage.TH -> "🇫🇷 ฝรั่งเศส"
    }

    fun tabAmerican(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇺🇸 American"
        AppLanguage.TH -> "🇺🇸 อเมริกัน"
    }

    fun tabThai(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇹🇭 Thai"
        AppLanguage.TH -> "🇹🇭 ไทย"
    }

    fun tabAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "All Dishes"
        AppLanguage.TH -> "ทั้งหมด"
    }

    fun cameraPermissionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Camera Permission Required"
        AppLanguage.TH -> "ต้องการสิทธิ์ใช้งานกล้อง"
    }

    fun cameraPermissionDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "To capture food photos and let AI estimate nutrients, please grant camera access or pick an image from your gallery."
        AppLanguage.TH -> "เพื่อถ่ายภาพอาหารและให้ AI คำนวณสารอาหารได้อย่างแม่นยำ กรุณาอนุญาตการเข้าถึงกล้อง หรือเลือกรูปภาพจากคลัง"
    }

    fun grantPermission(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Grant Camera Permission"
        AppLanguage.TH -> "อนุญาตการใช้กล้อง"
    }

    fun pickGallery(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select from Gallery"
        AppLanguage.TH -> "เลือกรูปจากคลังภาพ"
    }

    // Result Screen
    fun analysisComplete(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "AI Nutrition Analysis Complete"
        AppLanguage.TH -> "AI วิเคราะห์สารอาหารสำเร็จ"
    }

    fun editFoodInfo(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Edit Food Information"
        AppLanguage.TH -> "แก้ไขข้อมูลอาหาร"
    }

    fun foodNameLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Food Name"
        AppLanguage.TH -> "ชื่ออาหาร"
    }

    fun caloriesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Calories (kcal)"
        AppLanguage.TH -> "แคลอรี่ (kcal)"
    }

    fun portionLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Portion / Serving Size"
        AppLanguage.TH -> "ขนาดจาน / ปริมาณ"
    }

    fun done(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Done"
        AppLanguage.TH -> "เสร็จสิ้น"
    }

    fun portionPrefix(portion: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Serving: $portion"
        AppLanguage.TH -> "ปริมาณ: $portion"
    }

    fun selectMeal(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select Meal:"
        AppLanguage.TH -> "เลือกมื้ออาหาร:"
    }

    fun totalEnergy(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Total Energy"
        AppLanguage.TH -> "พลังงานทั้งหมด"
    }

    fun kcalUnit(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> " kcal"
        AppLanguage.TH -> " กิโลแคลอรี่ (kcal)"
    }

    fun macroTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Macronutrients"
        AppLanguage.TH -> "สารอาหารหลัก (Macronutrients)"
    }

    fun aiHealthTipTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "AI Nutrition & Health Tip"
        AppLanguage.TH -> "คำแนะนำโภชนาการจาก AI"
    }

    fun ingredientsDetected(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Detected Ingredients:"
        AppLanguage.TH -> "ส่วนประกอบที่ตรวจพบในจาน:"
    }

    fun saveToLog(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Save to Meal Log"
        AppLanguage.TH -> "บันทึกในรายการอาหาร"
    }

    fun retakeOrCancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Retake / Cancel"
        AppLanguage.TH -> "ถ่ายใหม่ / ยกเลิก"
    }

    // Detail Dialog
    fun loggedAt(time: String, portion: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Logged at: $time • Portion: $portion"
        AppLanguage.TH -> "บันทึกเมื่อ: $time • ปริมาณ: $portion"
    }

    fun deleteItem(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Delete Entry"
        AppLanguage.TH -> "ลบรายการนี้"
    }

    // History Screen
    fun historyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Meal History"
        AppLanguage.TH -> "ประวัติการรับประทานอาหาร"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Search foods e.g., chicken, salad..."
        AppLanguage.TH -> "ค้นหาชื่ออาหาร เช่น ข้าวมันไก่, สลัด..."
    }

    fun emptyHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "No logged meal history yet.\nWhen you capture and save meals, they will appear here."
        AppLanguage.TH -> "ยังไม่มีประวัติการบันทึกอาหาร\nเมื่อคุณถ่ายรูปและบันทึกอาหาร รายการจะแสดงที่นี่"
    }

    fun dayTotalCalories(calories: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Total $calories kcal"
        AppLanguage.TH -> "รวม $calories kcal"
    }

    // Analyzing Loading Dialog
    fun analyzingTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "AI Analyzing Nutrition..."
        AppLanguage.TH -> "AI กำลังคำนวณสารอาหาร..."
    }

    fun analyzingSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Detecting dish type, estimating serving portion, and calculating calories, protein, carbs & fat."
        AppLanguage.TH -> "ตรวจจับชนิดอาหาร ประมาณการสัดส่วน และคำนวณแคลอรี่ โปรตีน คาร์โบไฮเดรต ไขมัน"
    }
}
