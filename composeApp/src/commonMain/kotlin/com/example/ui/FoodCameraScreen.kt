package com.example.ui

import androidx.compose.runtime.Composable
import com.example.model.NutritionAnalysis
import com.example.util.AppLanguage

/**
 * Camera / photo-capture screen. Implemented separately per platform:
 * - Android: CameraX preview + PhotoPicker
 * - iOS: UIImagePickerController (camera + library)
 *
 * onImageCaptured delivers the raw JPEG bytes of the chosen/captured photo.
 */
@Composable
expect fun FoodCameraScreen(
    language: AppLanguage,
    onImageCaptured: (ByteArray) -> Unit,
    onSelectPreset: (NutritionAnalysis) -> Unit,
    onBack: () -> Unit
)
