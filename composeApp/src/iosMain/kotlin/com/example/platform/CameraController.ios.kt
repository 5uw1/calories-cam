package com.example.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.fileDataRepresentation
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSError
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue

/**
 * AVFoundation camera: live preview layer + still photo capture.
 * NOTE: only works on a real device. The iOS Simulator has no camera hardware
 * (AVCaptureDevice.defaultDeviceWithMediaType returns null → preview stays black).
 */
@OptIn(ExperimentalForeignApi::class)
class CameraController {
    private val session = AVCaptureSession()
    private val photoOutput = AVCapturePhotoOutput()
    private var captureDelegate: PhotoCaptureDelegate? = null

    val previewLayer: AVCaptureVideoPreviewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
        videoGravity = AVLayerVideoGravityResizeAspectFill
    }

    /** True only when running on hardware that actually has a camera. */
    val hasCameraHardware: Boolean
        get() = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) != null

    fun requestAccessAndConfigure(onReady: (granted: Boolean) -> Unit) {
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> {
                configureAndStart()
                onReady(true)
            }
            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    dispatch_async(dispatch_get_main_queue()) {
                        if (granted) configureAndStart()
                        onReady(granted)
                    }
                }
            }
            else -> onReady(false) // denied / restricted
        }
    }

    private fun configureAndStart() {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return
        session.beginConfiguration()
        session.sessionPreset = AVCaptureSessionPresetPhoto
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device, error = null)
        if (input != null && session.canAddInput(input)) session.addInput(input)
        if (session.canAddOutput(photoOutput)) session.addOutput(photoOutput)
        session.commitConfiguration()
        start()
    }

    fun start() {
        if (!session.running) {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
                session.startRunning()
            }
        }
    }

    fun stop() {
        if (session.running) {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
                session.stopRunning()
            }
        }
    }

    /** Capture a still photo; delivers JPEG bytes on the main thread (null on failure). */
    fun capture(onResult: (ByteArray?) -> Unit) {
        val delegate = PhotoCaptureDelegate { bytes ->
            captureDelegate = null
            dispatch_async(dispatch_get_main_queue()) { onResult(bytes) }
        }
        captureDelegate = delegate // strong ref until callback fires
        photoOutput.capturePhotoWithSettings(AVCapturePhotoSettings(), delegate)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class PhotoCaptureDelegate(
    private val onResult: (ByteArray?) -> Unit
) : NSObject(), AVCapturePhotoCaptureDelegateProtocol {

    override fun captureOutput(
        output: AVCapturePhotoOutput,
        didFinishProcessingPhoto: AVCapturePhoto,
        error: NSError?
    ) {
        val data = didFinishProcessingPhoto.fileDataRepresentation()
        onResult(data?.toByteArray())
    }
}
