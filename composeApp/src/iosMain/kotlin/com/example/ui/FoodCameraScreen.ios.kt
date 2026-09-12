package com.example.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.interop.UIKitView
import com.example.model.FoodSamples
import com.example.model.NutritionAnalysis
import com.example.model.displayFoodName
import com.example.platform.CameraController
import com.example.platform.toByteArray
import com.example.util.AppLanguage
import com.example.util.AppStrings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.darwin.NSObject
import platform.Foundation.NSData

/**
 * iOS capture screen with a LIVE AVFoundation camera preview (parity with Android CameraX),
 * an in-app shutter, and a photo-library fallback.
 *
 * Simulator note: no camera hardware exists, so the preview is black and only the
 * "choose from library" path works there — test the live camera on a real device.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun FoodCameraScreen(
    language: AppLanguage,
    onImageCaptured: (ByteArray) -> Unit,
    onSelectPreset: (NutritionAnalysis) -> Unit,
    onBack: () -> Unit
) {
    val controller = remember { CameraController() }
    var hasPermission by remember { mutableStateOf(false) }
    var hasHardware by remember { mutableStateOf(controller.hasCameraHardware) }
    var isCapturing by remember { mutableStateOf(false) }
    val pickerHolder = remember { mutableStateOf<ImagePickerDelegate?>(null) }

    LaunchedEffect(Unit) {
        controller.requestAccessAndConfigure { granted ->
            hasPermission = granted
            hasHardware = controller.hasCameraHardware
        }
    }
    DisposableEffect(Unit) { onDispose { controller.stop() } }

    fun openLibrary() {
        val root = topViewController() ?: return
        if (!UIImagePickerController.isSourceTypeAvailable(
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
        ) return
        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        val delegate = ImagePickerDelegate { image ->
            pickerHolder.value = null
            if (image != null) {
                val data: NSData? = UIImageJPEGRepresentation(image, 0.9)
                if (data != null) onImageCaptured(data.toByteArray())
            }
        }
        pickerHolder.value = delegate
        picker.delegate = delegate
        root.presentViewController(picker, animated = true, completion = null)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // Live camera preview (fills the screen). Black on simulator / when denied.
        if (hasPermission && hasHardware) {
            UIKitView(
                factory = { CameraPreviewView(controller.previewLayer) },
                modifier = Modifier.fillMaxSize()
            )
            FoodScannerReticle(
                language = language,
                modifier = Modifier.align(Alignment.Center).padding(bottom = 60.dp)
            )
        } else {
            // No live preview available — guide the user to the photo library instead.
            Column(
                modifier = Modifier.align(Alignment.Center).fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (!hasPermission) AppStrings.cameraPermissionTitle(language)
                           else AppStrings.scanInstruction(language),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = AppStrings.cameraPermissionDesc(language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Button(onClick = { openLibrary() }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(AppStrings.pickGallery(language))
                }
            }
        }

        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 44.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(44.dp).background(Color.Black.copy(alpha = 0.45f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Surface(color = Color.Black.copy(alpha = 0.55f), shape = RoundedCornerShape(20.dp)) {
                Text(
                    text = AppStrings.scanInstruction(language),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Spacer(Modifier.size(44.dp))
        }

        // Bottom controls
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f), Color.Black)
                    )
                )
                .padding(bottom = 36.dp, top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var selectedCuisineTab by remember { mutableStateOf("Swiss") }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.popularSamples(language),
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tabs = listOf(
                    "Swiss" to AppStrings.tabSwiss(language),
                    "Italian" to AppStrings.tabItalian(language),
                    "French" to AppStrings.tabFrench(language),
                    "American" to AppStrings.tabAmerican(language),
                    "Thai" to AppStrings.tabThai(language),
                    "All" to AppStrings.tabAll(language)
                )
                items(tabs) { (key, label) ->
                    val isSelected = selectedCuisineTab == key
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f),
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.White,
                        modifier = Modifier.clickable { selectedCuisineTab = key }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            val filteredDishes = remember(selectedCuisineTab) {
                when (selectedCuisineTab) {
                    "Swiss" -> FoodSamples.swissDishes
                    "Italian" -> FoodSamples.italianDishes
                    "French" -> FoodSamples.frenchDishes
                    "American" -> FoodSamples.americanDishes
                    "Thai" -> FoodSamples.thaiDishes
                    else -> FoodSamples.sampleDishes
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredDishes) { dish ->
                    val dishLabel = dish.displayFoodName(language)
                    val flagEmoji = when (dish.cuisine) {
                        "Swiss" -> "🇨🇭"; "Italian" -> "🇮🇹"; "French" -> "🇫🇷"
                        "American" -> "🇺🇸"; "Thai" -> "🇹🇭"; else -> "🍽️"
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f),
                        modifier = Modifier.clickable { onSelectPreset(dish) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = flagEmoji, style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$dishLabel (${dish.calories} kcal)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Shutter / library row
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { openLibrary() },
                    modifier = Modifier.size(52.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.PhotoLibrary, contentDescription = "Gallery",
                        tint = Color.White, modifier = Modifier.size(26.dp)
                    )
                }

                // Shutter: capture from the live camera
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(84.dp)
                        .border(4.dp, Color.White, CircleShape).padding(6.dp)
                        .background(
                            if (isCapturing || !(hasPermission && hasHardware)) Color.Gray
                            else MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .clickable(enabled = !isCapturing) {
                            if (hasPermission && hasHardware) {
                                isCapturing = true
                                controller.capture { bytes ->
                                    isCapturing = false
                                    if (bytes != null) onImageCaptured(bytes)
                                }
                            } else {
                                openLibrary()
                            }
                        }
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(36.dp))
                    }
                }

                Box(modifier = Modifier.size(52.dp))
            }
        }
    }
}

/** UIView whose sublayer is the camera preview, kept sized to the view's bounds. */
@OptIn(ExperimentalForeignApi::class)
private class CameraPreviewView(
    private val previewLayer: AVCaptureVideoPreviewLayer
) : UIView(frame = CGRectZero.readValue()) {
    init { layer.addSublayer(previewLayer) }

    override fun layoutSubviews() {
        super.layoutSubviews()
        CATransaction.begin()
        CATransaction.setValue(true, forKey = kCATransactionDisableActions)
        previewLayer.setFrame(bounds)
        CATransaction.commit()
    }
}

/** Top-most view controller without the deprecated keyWindow (which returns null under Compose). */
@OptIn(ExperimentalForeignApi::class)
private fun topViewController(): UIViewController? {
    val windows = UIApplication.sharedApplication.windows
    val keyWindow = windows.firstOrNull { (it as? UIWindow)?.keyWindow == true } as? UIWindow
        ?: windows.firstOrNull() as? UIWindow
    var vc = keyWindow?.rootViewController
    while (vc?.presentedViewController != null) vc = vc.presentedViewController
    return vc
}

/** Bridges UIImagePickerController callbacks back into Kotlin. */
private class ImagePickerDelegate(
    private val onResult: (UIImage?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        picker.dismissViewControllerAnimated(true, completion = null)
        onResult(image)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        onResult(null)
    }
}

@Composable
fun FoodScannerReticle(
    language: AppLanguage = AppLanguage.EN,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ScanningTransition")
    val scanLineProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScanLineProgress"
    )

    Box(modifier = modifier.size(280.dp)) {
        val cornerSize = 28.dp
        val strokeWidth = 3.5.dp
        val reticleColor = MaterialTheme.colorScheme.primary

        Box(Modifier.align(Alignment.TopStart).size(cornerSize)
            .border(strokeWidth, reticleColor, RoundedCornerShape(topStart = 8.dp)))
        Box(Modifier.align(Alignment.TopEnd).size(cornerSize)
            .border(strokeWidth, reticleColor, RoundedCornerShape(topEnd = 8.dp)))
        Box(Modifier.align(Alignment.BottomStart).size(cornerSize)
            .border(strokeWidth, reticleColor, RoundedCornerShape(bottomStart = 8.dp)))
        Box(Modifier.align(Alignment.BottomEnd).size(cornerSize)
            .border(strokeWidth, reticleColor, RoundedCornerShape(bottomEnd = 8.dp)))

        Text(
            text = AppStrings.reticleGuide(language),
            color = Color.White.copy(alpha = 0.75f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth().height(2.5.dp).align(Alignment.TopCenter)
                .padding(top = (280 * scanLineProgress).dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent, MaterialTheme.colorScheme.primary,
                            Color.White, MaterialTheme.colorScheme.primary, Color.Transparent
                        )
                    )
                )
        )
    }
}
