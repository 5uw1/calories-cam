package com.example.platform

import androidx.compose.ui.graphics.ImageBitmap

/** Decode JPEG/PNG bytes into a Compose ImageBitmap (skia on iOS, Bitmap on Android). */
expect fun decodeImageBitmap(bytes: ByteArray): ImageBitmap?
