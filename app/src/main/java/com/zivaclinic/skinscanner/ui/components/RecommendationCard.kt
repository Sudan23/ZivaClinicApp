package com.zivaclinic.skinscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zivaclinic.skinscanner.data.model.Recommendation
import com.zivaclinic.skinscanner.data.model.RecommendationType

@Composable
fun RecommendationCard(
    recommendation: Recommendation,
    modifier: Modifier = Modifier
) {
    val (icon, iconColor, categoryColor) = when (recommendation.category) {
        RecommendationType.TREATMENT -> Triple(
            Icons.Default.LocalHospital,
            Color(0xFF2196F3),
            Color(0xFF2196F3).copy(alpha = 0.1f)
        )
        RecommendationType.PRODUCT -> Triple(
            Icons.Default.Spa,
            Color(0xFF9C27B0),
            Color(0xFF9C27B0).copy(alpha = 0.1f)
        )
        RecommendationType.LIFESTYLE -> Triple(
            Icons.Default.Restaurant,
            Color(0xFF4CAF50),
            Color(0xFF4CAF50).copy(alpha = 0.1f)
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(categoryColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = recommendation.category.name,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recommendation.category.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = iconColor,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recommendation.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
