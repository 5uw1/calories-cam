package com.example.util

object AppStrings {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "CalorieCam"
        AppLanguage.TH -> "CalorieCam"
        AppLanguage.DE -> "CalorieCam"
    }

    // Menu options
    fun menuSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Daily Goal Setting"
        AppLanguage.TH -> "ตั้งเป้าหมายแคลอรี่"
        AppLanguage.DE -> "Tagesziel einstellen"
    }

    fun menuHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "History"
        AppLanguage.TH -> "ประวัติอาหาร"
        AppLanguage.DE -> "Verlauf"
    }

    /** Label for the row that opens the language picker. */
    fun menuLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Language"
        AppLanguage.TH -> "ภาษา"
        AppLanguage.DE -> "Sprache"
    }

    fun chooseLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Choose Language"
        AppLanguage.TH -> "เลือกภาษา"
        AppLanguage.DE -> "Sprache wählen"
    }

    fun menuTheme(isDark: Boolean, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> if (isDark) "Light Mode" else "Dark Mode"
        AppLanguage.TH -> if (isDark) "โหมดสว่าง" else "โหมดมืด"
        AppLanguage.DE -> if (isDark) "Heller Modus" else "Dunkler Modus"
    }

    // Navigation
    fun navToday(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Today"
        AppLanguage.TH -> "วันนี้"
        AppLanguage.DE -> "Heute"
    }

    fun navScan(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan Food"
        AppLanguage.TH -> "สแกนอาหาร"
        AppLanguage.DE -> "Essen scannen"
    }

    fun navHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "History"
        AppLanguage.TH -> "ประวัติ"
        AppLanguage.DE -> "Verlauf"
    }

    // Dashboard
    fun dailyEnergy(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Today's Intake"
        AppLanguage.TH -> "พลังงานวันนี้"
        AppLanguage.DE -> "Heutige Aufnahme"
    }

    fun remainingCalories(remaining: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "$remaining kcal remaining"
        AppLanguage.TH -> "เหลือได้อีก $remaining kcal"
        AppLanguage.DE -> "$remaining kcal übrig"
    }

    fun exceededCalories(exceeded: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "$exceeded kcal over goal"
        AppLanguage.TH -> "เกินเป้าหมาย $exceeded kcal"
        AppLanguage.DE -> "$exceeded kcal über dem Ziel"
    }

    fun protein(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Protein"
        AppLanguage.TH -> "โปรตีน"
        AppLanguage.DE -> "Protein"
    }

    fun carbs(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Carbs"
        AppLanguage.TH -> "คาร์บ"
        AppLanguage.DE -> "Kohlenhydrate"
    }

    fun fat(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Fat"
        AppLanguage.TH -> "ไขมัน"
        AppLanguage.DE -> "Fett"
    }

    fun fiber(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Fiber"
        AppLanguage.TH -> "ใยอาหาร"
        AppLanguage.DE -> "Ballaststoffe"
    }

    fun sugar(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Sugar"
        AppLanguage.TH -> "น้ำตาล"
        AppLanguage.DE -> "Zucker"
    }

    fun sodium(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Sodium"
        AppLanguage.TH -> "โซเดียม"
        AppLanguage.DE -> "Natrium"
    }

    fun todayFoodLog(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Today's Meals ($count items)"
        AppLanguage.TH -> "รายการอาหารวันนี้ ($count รายการ)"
        AppLanguage.DE -> "Heutige Mahlzeiten ($count Einträge)"
    }

    fun emptyLogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "No meals logged today"
        AppLanguage.TH -> "ยังไม่มีบันทึกอาหารวันนี้"
        AppLanguage.DE -> "Heute noch keine Mahlzeiten erfasst"
    }

    fun emptyLogSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Take a photo of your food or select a dish, and AI will estimate calories & nutrients automatically."
        AppLanguage.TH -> "ใช้กล้องถ่ายรูปอาหารของคุณ แล้ว AI จะช่วยคำนวณสารอาหารและแคลอรี่ให้โดยอัตโนมัติ"
        AppLanguage.DE -> "Fotografiere dein Essen oder wähle ein Gericht aus, und die KI berechnet automatisch Kalorien und Nährstoffe."
    }

    fun openScanner(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan Meal with Camera"
        AppLanguage.TH -> "เปิดกล้องถ่ายรูปอาหาร"
        AppLanguage.DE -> "Mahlzeit mit Kamera scannen"
    }

    fun setGoalTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Set Daily Calorie Target"
        AppLanguage.TH -> "ปรับเป้าหมายแคลอรี่ประจำวัน"
        AppLanguage.DE -> "Tägliches Kalorienziel festlegen"
    }

    fun setGoalDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Enter your target calories per day (kcal):"
        AppLanguage.TH -> "กำหนดปริมาณแคลอรี่ที่คุณต้องการต่อวัน (kcal):"
        AppLanguage.DE -> "Gib dein tägliches Kalorienziel ein (kcal):"
    }

    fun targetCalorie(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Target (kcal)"
        AppLanguage.TH -> "เป้าหมาย (kcal)"
        AppLanguage.DE -> "Ziel (kcal)"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Save"
        AppLanguage.TH -> "บันทึก"
        AppLanguage.DE -> "Speichern"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Cancel"
        AppLanguage.TH -> "ยกเลิก"
        AppLanguage.DE -> "Abbrechen"
    }

    fun close(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Close"
        AppLanguage.TH -> "ปิด"
        AppLanguage.DE -> "Schließen"
    }

    fun energy(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Energy"
        AppLanguage.TH -> "พลังงาน"
        AppLanguage.DE -> "Energie"
    }

    // Meal types
    fun mealBreakfast(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Breakfast"
        AppLanguage.TH -> "มื้อเช้า"
        AppLanguage.DE -> "Frühstück"
    }

    fun mealLunch(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Lunch"
        AppLanguage.TH -> "มื้อเที่ยง"
        AppLanguage.DE -> "Mittagessen"
    }

    fun mealDinner(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Dinner"
        AppLanguage.TH -> "มื้อเย็น"
        AppLanguage.DE -> "Abendessen"
    }

    fun mealSnack(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Snack"
        AppLanguage.TH -> "ของว่าง"
        AppLanguage.DE -> "Snack"
    }

    fun mealList(lang: AppLanguage): List<String> = listOf(
        mealBreakfast(lang),
        mealLunch(lang),
        mealDinner(lang),
        mealSnack(lang)
    )

    fun translateMeal(meal: String, lang: AppLanguage): String {
        return when (meal.trim().lowercase()) {
            "มื้อเช้า", "breakfast", "frühstück" -> mealBreakfast(lang)
            "มื้อเที่ยง", "lunch", "mittagessen" -> mealLunch(lang)
            "มื้อเย็น", "dinner", "abendessen" -> mealDinner(lang)
            "ของว่าง", "snack" -> mealSnack(lang)
            else -> meal
        }
    }

    // Camera Screen
    fun scanInstruction(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Snap food to calculate calories"
        AppLanguage.TH -> "ถ่ายรูปอาหารเพื่อคำนวณแคลอรี่"
        AppLanguage.DE -> "Foto vom Essen machen, um Kalorien zu berechnen"
    }

    fun reticleGuide(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Align food within the frame"
        AppLanguage.TH -> "จัดให้อาหารอยู่ภายในกรอบ"
        AppLanguage.DE -> "Essen im Rahmen ausrichten"
    }

    fun popularSamples(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "💡 Quick Presets (Tap to scan instantly):"
        AppLanguage.TH -> "💡 เมนูแนะนำ (แตะเพื่อสแกนทันที):"
        AppLanguage.DE -> "💡 Schnellauswahl (Zum sofortigen Scannen tippen):"
    }

    fun tabSwiss(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇨🇭 Swiss"
        AppLanguage.TH -> "🇨🇭 สวิส"
        AppLanguage.DE -> "🇨🇭 Schweizer"
    }

    fun tabItalian(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇮🇹 Italian"
        AppLanguage.TH -> "🇮🇹 อิตาเลียน"
        AppLanguage.DE -> "🇮🇹 Italienisch"
    }

    fun tabFrench(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇫🇷 French"
        AppLanguage.TH -> "🇫🇷 ฝรั่งเศส"
        AppLanguage.DE -> "🇫🇷 Französisch"
    }

    fun tabAmerican(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇺🇸 American"
        AppLanguage.TH -> "🇺🇸 อเมริกัน"
        AppLanguage.DE -> "🇺🇸 Amerikanisch"
    }

    fun tabThai(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "🇹🇭 Thai"
        AppLanguage.TH -> "🇹🇭 ไทย"
        AppLanguage.DE -> "🇹🇭 Thailändisch"
    }

    fun tabAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "All Dishes"
        AppLanguage.TH -> "ทั้งหมด"
        AppLanguage.DE -> "Alle Gerichte"
    }

    fun cameraPermissionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Camera Permission Required"
        AppLanguage.TH -> "ต้องการสิทธิ์ใช้งานกล้อง"
        AppLanguage.DE -> "Kamerazugriff erforderlich"
    }

    fun cameraPermissionDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "To capture food photos and let AI estimate nutrients, please grant camera access or pick an image from your gallery."
        AppLanguage.TH -> "เพื่อถ่ายภาพอาหารและให้ AI คำนวณสารอาหารได้อย่างแม่นยำ กรุณาอนุญาตการเข้าถึงกล้อง หรือเลือกรูปภาพจากคลัง"
        AppLanguage.DE -> "Um Essensfotos aufzunehmen und der KI die Nährstoffschätzung zu ermöglichen, erlaube bitte den Kamerazugriff oder wähle ein Bild aus deiner Galerie."
    }

    fun grantPermission(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Grant Camera Permission"
        AppLanguage.TH -> "อนุญาตการใช้กล้อง"
        AppLanguage.DE -> "Kamerazugriff erlauben"
    }

    fun pickGallery(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select from Gallery"
        AppLanguage.TH -> "เลือกรูปจากคลังภาพ"
        AppLanguage.DE -> "Aus Galerie auswählen"
    }

    // Result Screen
    fun analysisComplete(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "AI Nutrition Analysis Complete"
        AppLanguage.TH -> "AI วิเคราะห์สารอาหารสำเร็จ"
        AppLanguage.DE -> "KI-Nährwertanalyse abgeschlossen"
    }

    fun editFoodInfo(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Edit Food Information"
        AppLanguage.TH -> "แก้ไขข้อมูลอาหาร"
        AppLanguage.DE -> "Essensinformationen bearbeiten"
    }

    fun foodNameLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Food Name"
        AppLanguage.TH -> "ชื่ออาหาร"
        AppLanguage.DE -> "Name des Gerichts"
    }

    fun originalNameLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Original name"
        AppLanguage.TH -> "ชื่อดั้งเดิม"
        AppLanguage.DE -> "Originalname"
    }

    fun caloriesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Calories (kcal)"
        AppLanguage.TH -> "แคลอรี่ (kcal)"
        AppLanguage.DE -> "Kalorien (kcal)"
    }

    fun portionLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Portion / Serving Size"
        AppLanguage.TH -> "ขนาดจาน / ปริมาณ"
        AppLanguage.DE -> "Portion / Portionsgröße"
    }

    fun done(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Done"
        AppLanguage.TH -> "เสร็จสิ้น"
        AppLanguage.DE -> "Fertig"
    }

    fun portionPrefix(portion: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Serving: $portion"
        AppLanguage.TH -> "ปริมาณ: $portion"
        AppLanguage.DE -> "Portion: $portion"
    }

    fun selectMeal(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select Meal:"
        AppLanguage.TH -> "เลือกมื้ออาหาร:"
        AppLanguage.DE -> "Mahlzeit auswählen:"
    }

    fun totalEnergy(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Total Energy"
        AppLanguage.TH -> "พลังงานทั้งหมด"
        AppLanguage.DE -> "Gesamtenergie"
    }

    fun kcalUnit(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> " kcal"
        AppLanguage.TH -> " กิโลแคลอรี่ (kcal)"
        AppLanguage.DE -> " kcal"
    }

    fun macroTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Macronutrients"
        AppLanguage.TH -> "สารอาหารหลัก (Macronutrients)"
        AppLanguage.DE -> "Makronährstoffe"
    }

    fun aiHealthTipTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "AI Nutrition & Health Tip"
        AppLanguage.TH -> "คำแนะนำโภชนาการจาก AI"
        AppLanguage.DE -> "KI-Ernährungs- und Gesundheitstipp"
    }

    fun ingredientsDetected(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Detected Ingredients:"
        AppLanguage.TH -> "ส่วนประกอบที่ตรวจพบในจาน:"
        AppLanguage.DE -> "Erkannte Zutaten:"
    }

    fun saveToLog(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Save to Meal Log"
        AppLanguage.TH -> "บันทึกในรายการอาหาร"
        AppLanguage.DE -> "Im Essensprotokoll speichern"
    }

    fun retakeOrCancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Retake / Cancel"
        AppLanguage.TH -> "ถ่ายใหม่ / ยกเลิก"
        AppLanguage.DE -> "Erneut aufnehmen / Abbrechen"
    }

    // Detail Dialog
    fun loggedAt(time: String, portion: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Logged at: $time • Portion: $portion"
        AppLanguage.TH -> "บันทึกเมื่อ: $time • ปริมาณ: $portion"
        AppLanguage.DE -> "Erfasst um: $time • Portion: $portion"
    }

    fun deleteItem(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Delete Entry"
        AppLanguage.TH -> "ลบรายการนี้"
        AppLanguage.DE -> "Eintrag löschen"
    }

    // History Screen
    fun historyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Meal History"
        AppLanguage.TH -> "ประวัติการรับประทานอาหาร"
        AppLanguage.DE -> "Mahlzeitenverlauf"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Search foods e.g., chicken, salad..."
        AppLanguage.TH -> "ค้นหาชื่ออาหาร เช่น ข้าวมันไก่, สลัด..."
        AppLanguage.DE -> "Gerichte suchen, z. B. Hähnchen, Salat..."
    }

    fun emptyHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "No logged meal history yet.\nWhen you capture and save meals, they will appear here."
        AppLanguage.TH -> "ยังไม่มีประวัติการบันทึกอาหาร\nเมื่อคุณถ่ายรูปและบันทึกอาหาร รายการจะแสดงที่นี่"
        AppLanguage.DE -> "Noch kein Mahlzeitenverlauf.\nWenn du Mahlzeiten aufnimmst und speicherst, erscheinen sie hier."
    }

    fun dayTotalCalories(calories: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Total $calories kcal"
        AppLanguage.TH -> "รวม $calories kcal"
        AppLanguage.DE -> "Gesamt $calories kcal"
    }

    // Analyzing Loading Dialog
    fun analyzingTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "AI Analyzing Nutrition..."
        AppLanguage.TH -> "AI กำลังคำนวณสารอาหาร..."
        AppLanguage.DE -> "KI analysiert Nährwerte..."
    }

    fun analyzingSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Detecting dish type, estimating serving portion, and calculating calories, protein, carbs & fat."
        AppLanguage.TH -> "ตรวจจับชนิดอาหาร ประมาณการสัดส่วน และคำนวณแคลอรี่ โปรตีน คาร์โบไฮเดรต ไขมัน"
        AppLanguage.DE -> "Erkennt die Gerichtsart, schätzt die Portionsgröße und berechnet Kalorien, Protein, Kohlenhydrate und Fett."
    }
}
