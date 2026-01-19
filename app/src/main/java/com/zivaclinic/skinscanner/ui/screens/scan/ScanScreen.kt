package com.zivaclinic.skinscanner.ui.screens.scan

import android.Manifest
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import com.zivaclinic.skinscanner.data.model.CaptureAngle
import com.zivaclinic.skinscanner.ui.components.CameraPreview
import com.zivaclinic.skinscanner.ui.components.FaceGuideOverlay
import com.zivaclinic.skinscanner.utils.FaceAnalyzer
import com.zivaclinic.skinscanner.utils.ImageProcessor
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: ScanViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    // Navigate to results when processing is complete
    LaunchedEffect(state.scanResult) {
        if (state.scanResult != null) {
            navController.navigate("results/${state.scanResult!!.id}") {
                popUpTo("scan") { inclusive = true }
            }
        }
    }

    if (!cameraPermission.status.isGranted) {
        PermissionDeniedScreen(
            onRequestPermission = { cameraPermission.launchPermissionRequest() },
            onBack = { navController.popBackStack() }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Face Scan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isProcessing) {
                ProcessingOverlay()
            } else {
                CameraView(
                    onCameraReady = { preview ->
                        previewView = preview
                        setupCamera(
                            context,
                            lifecycleOwner,
                            preview,
                            viewModel,
                            onImageCaptureReady = { capture ->
                                imageCapture = capture
                            }
                        )
                    }
                )

                FaceGuideOverlay(
                    modifier = Modifier.fillMaxSize(),
                    isFaceDetected = state.isFaceDetected,
                    instruction = state.currentAngle.instruction
                )

                CaptureControls(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    currentAngle = state.currentAngle,
                    capturedAngles = state.capturedAngles,
                    isFaceDetected = state.isFaceDetected,
                    isCapturing = state.isCapturing,
                    onCaptureClick = {
                        imageCapture?.let { capture ->
                            takePicture(capture, context, viewModel)
                        }
                    }
                )
            }

            state.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }
    }
}

@Composable
fun CameraView(
    onCameraReady: (PreviewView) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onCameraReady = onCameraReady
        )
    }
}

@Composable
fun CaptureControls(
    modifier: Modifier = Modifier,
    currentAngle: CaptureAngle,
    capturedAngles: Set<CaptureAngle>,
    isFaceDetected: Boolean,
    isCapturing: Boolean,
    onCaptureClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CaptureAngle.values().forEach { angle ->
                AngleIndicator(
                    angle = angle,
                    isCaptured = capturedAngles.contains(angle),
                    isActive = currentAngle == angle
                )
            }
        }

        // Capture button
        FloatingActionButton(
            onClick = onCaptureClick,
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            containerColor = if (isFaceDetected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            if (isCapturing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Capture",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun AngleIndicator(
    angle: CaptureAngle,
    isCaptured: Boolean,
    isActive: Boolean
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        color = when {
            isCaptured -> MaterialTheme.colorScheme.primary
            isActive -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (isCaptured) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Captured",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = when (angle) {
                        CaptureAngle.FRONT -> "F"
                        CaptureAngle.LEFT -> "L"
                        CaptureAngle.RIGHT -> "R"
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ProcessingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Analyzing Your Skin...",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "This will take just a moment",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun PermissionDeniedScreen(
    onRequestPermission: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Camera Permission Required",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "We need camera access to scan your face",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
            TextButton(onClick = onBack) {
                Text("Go Back")
            }
        }
    }
}

@androidx.camera.core.ExperimentalGetImage
private fun setupCamera(
    context: android.content.Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    previewView: PreviewView,
    viewModel: ScanViewModel,
    onImageCaptureReady: (ImageCapture) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = androidx.camera.core.Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val faceAnalyzer = FaceAnalyzer { detected, _ ->
            viewModel.onFaceDetected(detected)
        }

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), faceAnalyzer)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
            onImageCaptureReady(imageCapture)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(context))
}

@androidx.camera.core.ExperimentalGetImage
private fun takePicture(
    imageCapture: ImageCapture,
    context: android.content.Context,
    viewModel: ScanViewModel
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: androidx.camera.core.ImageProxy) {
                val bitmap = ImageProcessor.imageProxyToBitmap(image)
                image.close()
                
                if (bitmap != null) {
                    viewModel.captureImage(context, bitmap)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
        }
    )
}
