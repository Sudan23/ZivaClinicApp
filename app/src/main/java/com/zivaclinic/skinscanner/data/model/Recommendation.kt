package com.zivaclinic.skinscanner.data.model

enum class RecommendationType {
    TREATMENT, PRODUCT, LIFESTYLE
}

data class Recommendation(
    val category: RecommendationType,
    val title: String,
    val description: String
)
