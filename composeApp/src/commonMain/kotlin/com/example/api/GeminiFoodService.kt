package com.example.api

import com.example.model.FoodSamples
import com.example.model.NutritionAnalysis
import com.example.platform.ImageCodec
import com.example.platform.geminiApiKey
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull

/**
 * Gemini Vision food analysis, multiplatform via Ktor.
 * Accepts raw JPEG bytes (platform-neutral) instead of an android Bitmap.
 * Falls back to a random preset dish when the key is missing or the call fails,
 * exactly like the original Android implementation.
 */
class GeminiFoodService(private val client: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private companion object {
        const val BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-lite-latest:generateContent"
    }

    suspend fun analyzeFoodImage(imageBytes: ByteArray): Result<NutritionAnalysis> {
        val apiKey = geminiApiKey()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val fallback = FoodSamples.sampleDishes.random()
            return Result.success(
                fallback.copy(
                    imageBytes = imageBytes,
                    healthTip = fallback.healthTip +
                        " (โหมดจำลอง: ตั้งค่า Gemini API key เพื่อเปิดใช้งาน AI เต็มรูปแบบ)"
                )
            )
        }

        return try {
            val base64Image = ImageCodec.encodeToBase64(imageBytes)
            val body = buildRequestBody(base64Image)

            val response: HttpResponse = client.post("$BASE_URL?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(body.toString())
            }

            if (!response.status.isSuccess()) {
                val fallback = FoodSamples.sampleDishes.random()
                return Result.success(
                    fallback.copy(
                        imageBytes = imageBytes,
                        healthTip = fallback.healthTip + " (API ชั่วคราว: แสดงผลการประมาณการ)"
                    )
                )
            }

            val text = extractText(response.bodyAsText())
            if (text.isNullOrBlank()) {
                return Result.success(
                    FoodSamples.sampleDishes.random().copy(imageBytes = imageBytes)
                )
            }

            Result.success(parseAnalysis(text, imageBytes))
        } catch (e: Exception) {
            Result.success(
                FoodSamples.sampleDishes.random().copy(
                    imageBytes = imageBytes,
                    healthTip = "ระบบประมวลผลอัตโนมัติ: ดื่มน้ำและรับประทานผักผลไม้เพิ่มเพื่อสุขภาพที่ดี"
                )
            )
        }
    }

    private val prompt = """
        คุณคือนักกำหนดอาหารและผู้เชี่ยวชาญด้านโภชนาการ วิเคราะห์ภาพอาหารนี้อย่างละเอียด (สามารถระบุอาหารไทย, สวิส, อิตาเลียน, ฝรั่งเศส, อเมริกัน หรืออาหารนานาชาติอื่นๆ ได้อย่างแม่นยำ):
        1. ระบุชื่ออาหาร (foodName ภาษาไทย, foodNameEn ภาษาอังกฤษ)
        2. ประเมินขนาดจานหรือปริมาณ (portionSize เช่น 1 จาน (350g), 1 ถ้วย)
        3. คำนวณพลังงานรวม (calories หน่วย kcal เป็นจำนวนเต็ม)
        4. สารอาหารหลัก: โปรตีน (protein หน่วยกรัม), คาร์โบไฮเดรต (carbs หน่วยกรัม), ไขมัน (fat หน่วยกรัม)
        5. สารอาหารย่อย: ใยอาหาร (fiber หน่วยกรัม), น้ำตาล (sugar หน่วยกรัม), โซเดียม (sodium หน่วย mg)
        6. ระบุประเภทมื้ออาหารที่เหมาะสม (mealType: 'มื้อเช้า', 'มื้อเที่ยง', 'มื้อเย็น', หรือ 'ของว่าง')
        7. คำแนะนำด้านสุขภาพสั้นๆ (healthTip ภาษาไทย)
        8. รายการส่วนประกอบหลักที่มองเห็นในภาพ (ingredients)
        9. ระบุสัญชาติอาหาร (cuisine: 'Swiss', 'Italian', 'French', 'American', 'Thai', หรือ 'International')

        ตอบกลับเฉพาะ JSON object ที่มีโครงสร้างดังนี้เท่านั้น (ห้ามใส่ markdown หรือข้อความอื่น):
        {
          "foodName": "ชื่ออาหารภาษาไทย",
          "foodNameEn": "English Name",
          "portionSize": "1 จาน (350g)",
          "calories": 550,
          "protein": 28.5,
          "carbs": 65.0,
          "fat": 18.0,
          "fiber": 3.0,
          "sugar": 4.5,
          "sodium": 850,
          "mealType": "มื้อเที่ยง",
          "healthTip": "คำแนะนำโภชนาการ",
          "ingredients": ["ส่วนประกอบ 1", "ส่วนประกอบ 2"],
          "cuisine": "Italian"
        }
    """.trimIndent()

    private fun buildRequestBody(base64Image: String): JsonObject = buildJsonObject {
        putJsonArray("contents") {
            addJsonObject {
                putJsonArray("parts") {
                    addJsonObject { put("text", prompt) }
                    addJsonObject {
                        putJsonObject("inlineData") {
                            put("mimeType", "image/jpeg")
                            put("data", base64Image)
                        }
                    }
                }
            }
        }
        putJsonObject("generationConfig") {
            put("temperature", 0.4)
            put("responseMimeType", "application/json")
        }
    }

    private fun extractText(responseBody: String): String? = try {
        json.parseToJsonElement(responseBody).jsonObject["candidates"]
            ?.jsonArray?.firstOrNull()
            ?.jsonObject?.get("content")
            ?.jsonObject?.get("parts")
            ?.jsonArray?.firstOrNull()
            ?.jsonObject?.get("text")
            ?.jsonPrimitive?.contentOrNull
    } catch (e: Exception) {
        null
    }

    private fun parseAnalysis(text: String, imageBytes: ByteArray): NutritionAnalysis {
        val clean = text.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
        val obj = json.parseToJsonElement(clean).jsonObject

        fun str(key: String, def: String) = obj[key]?.jsonPrimitive?.contentOrNull ?: def
        fun int(key: String, def: Int) = obj[key]?.jsonPrimitive?.intOrNull ?: def
        fun flt(key: String, def: Double) =
            (obj[key]?.jsonPrimitive?.doubleOrNull ?: def).toFloat()

        val ingredients = obj["ingredients"]?.jsonArray
            ?.mapNotNull { it.jsonPrimitive.contentOrNull } ?: emptyList()

        return NutritionAnalysis(
            foodName = str("foodName", "อาหารเพื่อสุขภาพ"),
            foodNameEn = str("foodNameEn", "Healthy Food"),
            calories = int("calories", 450),
            protein = flt("protein", 20.0),
            carbs = flt("carbs", 50.0),
            fat = flt("fat", 15.0),
            fiber = flt("fiber", 2.0),
            sugar = flt("sugar", 3.0),
            sodium = int("sodium", 600),
            portionSize = str("portionSize", "1 จาน"),
            mealType = str("mealType", "มื้อเที่ยง"),
            healthTip = str("healthTip", "ควรดื่มน้ำอย่างน้อยวันละ 8 แก้ว และรับประทานอาหารให้หลากหลาย"),
            ingredients = ingredients,
            imageBytes = imageBytes,
            cuisine = str("cuisine", "International")
        )
    }
}
