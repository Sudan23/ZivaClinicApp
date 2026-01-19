package com.zivaclinic.skinscanner.utils

import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceAnalyzer(
    private val onFaceDetected: (Boolean, List<Face>) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setMinFaceSize(0.15f)
        .enableTracking()
        .build()

    private val detector = FaceDetection.getClient(options)

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            detector.process(image)
                .addOnSuccessListener { faces ->
                    val faceDetected = faces.isNotEmpty()
                    onFaceDetected(faceDetected, faces)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    onFaceDetected(false, emptyList())
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    fun isWellPositioned(face: Face, imageWidth: Int, imageHeight: Int): Boolean {
        val boundingBox = face.boundingBox
        val centerX = boundingBox.centerX()
        val centerY = boundingBox.centerY()
        
        val imageCenterX = imageWidth / 2
        val imageCenterY = imageHeight / 2
        
        val offsetX = Math.abs(centerX - imageCenterX).toFloat() / imageWidth
        val offsetY = Math.abs(centerY - imageCenterY).toFloat() / imageHeight
        
        // Face should be centered within 20% tolerance
        return offsetX < 0.2f && offsetY < 0.2f
    }

    fun isFaceSizeAcceptable(face: Face, imageWidth: Int, imageHeight: Int): Boolean {
        val boundingBox = face.boundingBox
        val faceWidth = boundingBox.width().toFloat()
        val faceHeight = boundingBox.height().toFloat()
        
        val widthRatio = faceWidth / imageWidth
        val heightRatio = faceHeight / imageHeight
        
        // Face should occupy 30-70% of the frame
        return widthRatio in 0.3f..0.7f && heightRatio in 0.3f..0.7f
    }
}
