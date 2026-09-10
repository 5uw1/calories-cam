# CalorieCam → Kotlin Multiplatform (iOS + Android) Migration

แปลงจากแอป **Android-only** (Kotlin/Compose/Room/CameraX/OkHttp) ไปเป็น
**Kotlin Multiplatform + Compose Multiplatform** ที่รันได้ทั้ง **Android และ iOS**
โดยเก็บโค้ด UI/logic เดิมไว้ใน `commonMain` เกือบทั้งหมด

---

## สถาปัตยกรรมใหม่

```
calories-cam/
├── composeApp/                     # โมดูล KMP หลัก (แทนที่ app/ เดิม)
│   ├── src/commonMain/             # โค้ดที่แชร์ทั้ง 2 แพลตฟอร์ม (~90%)
│   │   ├── kotlin/com/example/
│   │   │   ├── model/              # NutritionAnalysis, FoodEntry (Room entity)
│   │   │   ├── data/               # FoodDao, FoodRepository, FoodDatabase (Room KMP)
│   │   │   ├── api/                # GeminiFoodService (Ktor client)
│   │   │   ├── ui/                 # ทุกสกรีน Compose + FoodViewModel
│   │   │   └── util/               # LanguageManager, ThemeManager, DateFormat, ImageCodec
│   │   └── composeResources/
│   ├── src/androidMain/            # กล้อง CameraX, Room builder, Ktor OkHttp, MainActivity
│   └── src/iosMain/                # กล้อง UIImagePicker, Room builder, Ktor Darwin, entrypoint
├── iosApp/                         # Xcode project (SwiftUI shell + Info.plist)
└── app/                            # โมดูลเดิม — เก็บไว้อ้างอิง ลบได้เมื่อ verify แล้ว
```

## expect/actual (โค้ดเฉพาะแพลตฟอร์ม)

| ความสามารถ | commonMain (expect) | androidMain (actual) | iosMain (actual) |
|---|---|---|---|
| ถอดรหัสรูป → ImageBitmap | `decodeToImageBitmap(ByteArray)` | `BitmapFactory` | `UIImage`/skia |
| Base64 encode/decode | `ImageCodec` | `android.util.Base64` | `NSData base64` |
| Gemini API key | `geminiApiKey()` | `BuildConfig` | `Info.plist` |
| HTTP engine | Ktor `HttpClient` | OkHttp engine | Darwin engine |
| Room DB builder | `getFoodDatabase(builder)` | `Room.databaseBuilder(ctx)` | docs dir path |
| กล้อง/เลือกรูป | `FoodCameraScreen(...)` | CameraX + PhotoPicker | UIImagePickerController |
| เวลาปัจจุบัน/format วันที่ | `DateFormat` (kotlinx-datetime) | ← ใช้ร่วม | ← ใช้ร่วม |

## สิ่งที่เปลี่ยนจากของเดิม

1. **`NutritionAnalysis.bitmap: Bitmap?` → `imageBytes: ByteArray?`** (JPEG) — ตัด `android.graphics.Bitmap` ออกจาก common
2. **`GeminiFoodService`** เขียนใหม่ด้วย **Ktor + kotlinx.serialization** แทน OkHttp+org.json (คง prompt/fallback เดิมทุกอย่าง)
3. **Room** อัปเป็น **Room KMP 2.7** + `BundledSQLiteDriver` (จาก `androidx.sqlite`)
4. **`FoodViewModel`** เดิมเป็น `AndroidViewModel` → เป็น plain class ที่รับ `FoodRepository` + มี `CoroutineScope` ของตัวเอง (ไม่ผูก Android)
5. **date formatting** (`SimpleDateFormat`/`Calendar`) → helper ใน `util/DateFormat.kt` ด้วย `kotlinx-datetime` (รองรับชื่อวัน/เดือนไทย-อังกฤษเอง ไม่พึ่ง locale ของ OS)
6. **`collectAsStateWithLifecycle` → `collectAsState`** (ตัด dependency androidx.lifecycle)

---

## วิธีบิลด์ (ต้องทำบนเครื่องที่มี toolchain ครบ)

> เครื่องที่ตั้งโครงสร้างนี้ **ไม่มี** JDK/Android SDK/Xcode จึง build ไม่ได้ที่นี่
> ต้องเปิดบนเครื่องที่ติดตั้งครบ (แนะนำ macOS + Android Studio + Xcode)

### เตรียม
- JDK 17+, Android Studio (Ladybug+), Xcode 15+ (สำหรับ iOS), CocoaPods ไม่จำเป็น (ใช้ direct framework)
- ใส่ Gemini API key:
  - Android: เพิ่มใน `local.properties` → `geminiApiKey=xxx` หรือ env `GEMINI_API_KEY`
  - iOS: ใส่คีย์ `GEMINI_API_KEY` ใน `iosApp/Info.plist`

### Android
```bash
./gradlew :composeApp:assembleDebug        # ได้ APK
./gradlew :composeApp:installDebug         # ติดตั้งลงเครื่อง/emulator
```

### iOS
```bash
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64   # ตรวจว่า framework ลิงก์ผ่าน
# จากนั้นเปิด iosApp/iosApp.xcodeproj ใน Xcode แล้ว Run (เลือก simulator)
```

## สถานะ / สิ่งที่ต้อง verify ตอนบิลด์จริง
- [ ] Sync Gradle บน Android Studio (ดึง KMP/Room/Ktor plugins)
- [ ] Room KSP generate `FoodDatabaseConstructor` actual ให้ครบทั้ง 2 target
- [ ] Android: ขอ permission กล้อง + CameraX preview
- [ ] iOS: ตั้ง `NSCameraUsageDescription`, `NSPhotoLibraryUsageDescription` ใน Info.plist (ใส่ให้แล้ว)
- [ ] iOS framework embedding ใน Xcode (script ใส่ให้ใน build phase)
- [ ] ทดสอบเรียก Gemini จริงทั้ง 2 แพลตฟอร์ม (มี fallback preset ถ้าคีย์ว่าง)
