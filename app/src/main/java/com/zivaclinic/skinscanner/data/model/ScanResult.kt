package com.zivaclinic.skinscanner.data.model

import java.util.*

data class ScanResult(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val overallScore: Int,
    val skinAge: Int,
    val hydration: Int,
    val elasticity: Int,
    val skinType: String,
    val imagePaths: List<String>,
    val concerns: List<SkinConcern>,
    val recommendations: List<Recommendation>
)
