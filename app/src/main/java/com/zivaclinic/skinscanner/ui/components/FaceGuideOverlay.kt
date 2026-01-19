package com.zivaclinic.skinscanner.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun FaceGuideOverlay(
    modifier: Modifier = Modifier,
    isFaceDetected: Boolean = false,
    instruction: String = "Position your face in the guide"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val strokeColor = if (isFaceDetected) {
        Color(0xFF4CAF50)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = alpha)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            // Draw darkened background with oval cutout
            val ovalWidth = canvasWidth * 0.7f
            val ovalHeight = canvasHeight * 0.55f
            val ovalLeft = (canvasWidth - ovalWidth) / 2
            val ovalTop = (canvasHeight - ovalHeight) / 2

            val path = Path().apply {
                addRect(Rect(0f, 0f, canvasWidth, canvasHeight))
                addOval(
                    Rect(
                        ovalLeft,
                        ovalTop,
                        ovalLeft + ovalWidth,
                        ovalTop + ovalHeight
                    )
                )
            }

            drawPath(
                path = path,
                color = Color.Black.copy(alpha = 0.5f),
                style = Stroke(width = 0f)
            )

            // Draw oval guide
            drawOval(
                color = strokeColor,
                topLeft = Offset(ovalLeft, ovalTop),
                size = Size(ovalWidth, ovalHeight),
                style = Stroke(
                    width = 4.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f)),
                    cap = StrokeCap.Round
                )
            )

            // Draw corner markers
            val cornerLength = 40.dp.toPx()
            val cornerStroke = 6.dp.toPx()
            val corners = listOf(
                // Top-left
                Pair(
                    Offset(ovalLeft, ovalTop + cornerLength),
                    Offset(ovalLeft, ovalTop)
                ),
                Pair(
                    Offset(ovalLeft, ovalTop),
                    Offset(ovalLeft + cornerLength, ovalTop)
                ),
                // Top-right
                Pair(
                    Offset(ovalLeft + ovalWidth - cornerLength, ovalTop),
                    Offset(ovalLeft + ovalWidth, ovalTop)
                ),
                Pair(
                    Offset(ovalLeft + ovalWidth, ovalTop),
                    Offset(ovalLeft + ovalWidth, ovalTop + cornerLength)
                ),
                // Bottom-left
                Pair(
                    Offset(ovalLeft, ovalTop + ovalHeight - cornerLength),
                    Offset(ovalLeft, ovalTop + ovalHeight)
                ),
                Pair(
                    Offset(ovalLeft, ovalTop + ovalHeight),
                    Offset(ovalLeft + cornerLength, ovalTop + ovalHeight)
                ),
                // Bottom-right
                Pair(
                    Offset(ovalLeft + ovalWidth - cornerLength, ovalTop + ovalHeight),
                    Offset(ovalLeft + ovalWidth, ovalTop + ovalHeight)
                ),
                Pair(
                    Offset(ovalLeft + ovalWidth, ovalTop + ovalHeight),
                    Offset(ovalLeft + ovalWidth, ovalTop + ovalHeight - cornerLength)
                )
            )

            corners.forEach { (start, end) ->
                drawLine(
                    color = strokeColor,
                    start = start,
                    end = end,
                    strokeWidth = cornerStroke,
                    cap = StrokeCap.Round
                )
            }
        }

        // Instruction text
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = instruction,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        // Status indicator
        if (isFaceDetected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            ) {
                Text(
                    text = "✓ Face Detected",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}
