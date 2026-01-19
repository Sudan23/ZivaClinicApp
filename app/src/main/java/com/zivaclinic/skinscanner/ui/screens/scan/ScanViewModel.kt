package com.zivaclinic.skinscanner.ui.screens.scan

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivaclinic.skinscanner.data.model.CaptureAngle
import com.zivaclinic.skinscanner.data.model.ScanResult
import com.zivaclinic.skinscanner.data.repository.ScanRepository
import com.zivaclinic.skinscanner.utils.AnalysisEngine
import com.zivaclinic.skinscanner.utils.ImageProcessor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class ScanState(
    val currentAngle: CaptureAngle = CaptureAngle.FRONT,
    val capturedAngles: Set<CaptureAngle> = emptySet(),
    val isFaceDetected: Boolean = false,
    val isCapturing: Boolean = false,
    val isProcessing: Boolean = false,
    val capturedImages: Map<CaptureAngle, String> = emptyMap(),
    val scanResult: ScanResult? = null,
    val error: String? = null
)

class ScanViewModel(
    private val repository: ScanRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScanState())
    val state: StateFlow<ScanState> = _state.asStateFlow()

    fun onFaceDetected(detected: Boolean) {
        _state.value = _state.value.copy(isFaceDetected = detected)
    }

    fun captureImage(context: Context, bitmap: Bitmap) {
        if (_state.value.isCapturing) return

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isCapturing = true, error = null)

                val currentAngle = _state.value.currentAngle
                val filename = "scan_${UUID.randomUUID()}_${currentAngle.name.lowercase()}.jpg"
                
                val imagePath = ImageProcessor.saveImage(context, bitmap, filename)
                
                if (imagePath != null) {
                    val updatedImages = _state.value.capturedImages.toMutableMap()
                    updatedImages[currentAngle] = imagePath
                    
                    val updatedAngles = _state.value.capturedAngles.toMutableSet()
                    updatedAngles.add(currentAngle)

                    _state.value = _state.value.copy(
                        capturedImages = updatedImages,
                        capturedAngles = updatedAngles,
                        isCapturing = false
                    )

                    // Move to next angle or process
                    if (updatedAngles.size < CaptureAngle.values().size) {
                        moveToNextAngle()
                    } else {
                        processImages(context)
                    }
                } else {
                    _state.value = _state.value.copy(
                        isCapturing = false,
                        error = "Failed to save image"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isCapturing = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun moveToNextAngle() {
        val angles = CaptureAngle.values()
        val currentIndex = angles.indexOf(_state.value.currentAngle)
        val nextIndex = (currentIndex + 1) % angles.size
        
        val nextAngle = angles.firstOrNull { angle ->
            angle !in _state.value.capturedAngles
        } ?: angles[nextIndex]

        _state.value = _state.value.copy(currentAngle = nextAngle)
    }

    private suspend fun processImages(context: Context) {
        _state.value = _state.value.copy(isProcessing = true, error = null)
        
        try {
            // Simulate processing time
            delay(2000)

            val imagePaths = _state.value.capturedImages.values.toList()
            val scanResult = AnalysisEngine.generateMockAnalysis(imagePaths)

            // Save to database
            repository.insertScan(scanResult)

            _state.value = _state.value.copy(
                isProcessing = false,
                scanResult = scanResult
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isProcessing = false,
                error = e.message ?: "Failed to process images"
            )
        }
    }

    fun resetScan() {
        _state.value = ScanState()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
