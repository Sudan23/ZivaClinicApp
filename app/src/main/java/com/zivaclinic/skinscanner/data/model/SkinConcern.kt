package com.zivaclinic.skinscanner.data.model

enum class ConcernType {
    WRINKLES, DARK_SPOTS, ACNE, REDNESS, DRYNESS, 
    PORES, TEXTURE, PIGMENTATION, SCARS
}

enum class Severity {
    MILD, MODERATE, SEVERE
}

data class SkinConcern(
    val type: ConcernType,
    val severity: Severity,
    val affectedArea: String,
    val percentage: Int,
    val description: String
)
