package com.zivaclinic.skinscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zivaclinic.skinscanner.data.model.Severity
import com.zivaclinic.skinscanner.data.model.SkinConcern

@Composable
fun ConcernCard(
    concern: SkinConcern,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = concern.type.name.replace("_", " "),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                SeverityBadge(severity = concern.severity)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Affected Area: ${concern.affectedArea}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = concern.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Percentage bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Coverage",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${concern.percentage}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(concern.percentage / 100f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(getSeverityColor(concern.severity))
                    )
                }
            }
        }
    }
}

@Composable
fun SeverityBadge(severity: Severity) {
    val (color, text) = when (severity) {
        Severity.MILD -> Color(0xFF4CAF50) to "Mild"
        Severity.MODERATE -> Color(0xFFFF9800) to "Moderate"
        Severity.SEVERE -> Color(0xFFF44336) to "Severe"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

fun getSeverityColor(severity: Severity): Color {
    return when (severity) {
        Severity.MILD -> Color(0xFF4CAF50)
        Severity.MODERATE -> Color(0xFFFF9800)
        Severity.SEVERE -> Color(0xFFF44336)
    }
}
