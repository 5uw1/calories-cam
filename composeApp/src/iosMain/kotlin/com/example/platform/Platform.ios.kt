package com.example.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual object ImageCodec {

    actual fun encodeToBase64(bytes: ByteArray): String =
        bytes.toNSData().base64EncodedStringWithOptions(0u)

    actual fun decodeFromBase64(base64: String): ByteArray {
        val nsData = NSData.create(base64EncodedString = base64, options = 0u)
            ?: return ByteArray(0)
        return nsData.toByteArray()
    }
}

/** Gemini key from the app's Info.plist (GEMINI_API_KEY). */
actual fun geminiApiKey(): String =
    (NSBundle.mainBundle.objectForInfoDictionaryKey("GEMINI_API_KEY") as? String) ?: ""

@OptIn(ExperimentalForeignApi::class)
internal fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData()
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val result = ByteArray(size)
    if (size == 0) return result
    result.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytes, length)
    }
    return result
}
