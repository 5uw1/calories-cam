package com.example.platform

import android.util.Base64
import com.example.BuildConfig

actual object ImageCodec {
    actual fun encodeToBase64(bytes: ByteArray): String =
        Base64.encodeToString(bytes, Base64.NO_WRAP)

    actual fun decodeFromBase64(base64: String): ByteArray =
        Base64.decode(base64, Base64.DEFAULT)
}

actual fun geminiApiKey(): String = try {
    BuildConfig.GEMINI_API_KEY
} catch (e: Throwable) {
    ""
}
