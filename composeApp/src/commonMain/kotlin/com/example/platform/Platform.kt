package com.example.platform

/** Base64 encode/decode — platform-backed (android.util.Base64 / NSData). */
expect object ImageCodec {
    fun encodeToBase64(bytes: ByteArray): String
    fun decodeFromBase64(base64: String): ByteArray
}

/** Gemini API key sourced per-platform (Android BuildConfig / iOS Info.plist). */
expect fun geminiApiKey(): String
