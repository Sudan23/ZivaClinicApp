package com.zivaclinic.skinscanner.utils

import com.zivaclinic.skinscanner.data.model.*
import kotlin.random.Random

object AnalysisEngine {

    fun generateMockAnalysis(imagePaths: List<String>): ScanResult {
        val overallScore = Random.nextInt(65, 95)
        val skinAge = Random.nextInt(22, 45)
        val hydration = Random.nextInt(60, 95)
        val elasticity = Random.nextInt(65, 90)
        
        val skinTypes = listOf("Normal", "Oily", "Dry", "Combination", "Sensitive")
        val skinType = skinTypes.random()
        
        val concerns = generateConcerns()
        val recommendations = generateRecommendations(concerns, skinType)
        
        return ScanResult(
            overallScore = overallScore,
            skinAge = skinAge,
            hydration = hydration,
            elasticity = elasticity,
            skinType = skinType,
            imagePaths = imagePaths,
            concerns = concerns,
            recommendations = recommendations
        )
    }

    private fun generateConcerns(): List<SkinConcern> {
        val possibleConcerns = listOf(
            SkinConcern(
                type = ConcernType.WRINKLES,
                severity = Severity.MILD,
                affectedArea = "Forehead and crow's feet",
                percentage = Random.nextInt(10, 25),
                description = "Fine lines are starting to appear, especially around the eyes and forehead."
            ),
            SkinConcern(
                type = ConcernType.DARK_SPOTS,
                severity = Severity.MODERATE,
                affectedArea = "Cheeks and temples",
                percentage = Random.nextInt(15, 30),
                description = "Sun-induced hyperpigmentation detected in exposed areas."
            ),
            SkinConcern(
                type = ConcernType.PORES,
                severity = Severity.MILD,
                affectedArea = "T-zone",
                percentage = Random.nextInt(20, 35),
                description = "Enlarged pores visible in the nose and forehead area."
            ),
            SkinConcern(
                type = ConcernType.DRYNESS,
                severity = Severity.MODERATE,
                affectedArea = "Cheeks",
                percentage = Random.nextInt(15, 28),
                description = "Skin appears dehydrated with some flaking."
            ),
            SkinConcern(
                type = ConcernType.REDNESS,
                severity = Severity.MILD,
                affectedArea = "Nose and cheeks",
                percentage = Random.nextInt(10, 20),
                description = "Mild redness indicating possible sensitivity or irritation."
            )
        )
        
        return possibleConcerns.shuffled().take(Random.nextInt(2, 4))
    }

    private fun generateRecommendations(
        concerns: List<SkinConcern>,
        skinType: String
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Treatment recommendations based on concerns
        if (concerns.any { it.type == ConcernType.WRINKLES }) {
            recommendations.add(
                Recommendation(
                    category = RecommendationType.TREATMENT,
                    title = "Anti-Aging Facial",
                    description = "Professional treatment with retinol and peptides to reduce fine lines and improve skin texture."
                )
            )
        }
        
        if (concerns.any { it.type == ConcernType.DARK_SPOTS }) {
            recommendations.add(
                Recommendation(
                    category = RecommendationType.TREATMENT,
                    title = "Brightening Treatment",
                    description = "Chemical peel or laser therapy to reduce hyperpigmentation and even skin tone."
                )
            )
        }
        
        if (concerns.any { it.type == ConcernType.PORES }) {
            recommendations.add(
                Recommendation(
                    category = RecommendationType.TREATMENT,
                    title = "Deep Cleansing Facial",
                    description = "Professional extraction and pore refining treatment to minimize pore appearance."
                )
            )
        }
        
        // Product recommendations
        recommendations.add(
            Recommendation(
                category = RecommendationType.PRODUCT,
                title = "Daily Sunscreen SPF 50+",
                description = "Essential protection against UV damage. Apply every morning and reapply throughout the day."
            )
        )
        
        when (skinType) {
            "Dry" -> {
                recommendations.add(
                    Recommendation(
                        category = RecommendationType.PRODUCT,
                        title = "Hydrating Serum with Hyaluronic Acid",
                        description = "Deeply moisturizes and plumps the skin, reducing the appearance of fine lines."
                    )
                )
            }
            "Oily" -> {
                recommendations.add(
                    Recommendation(
                        category = RecommendationType.PRODUCT,
                        title = "Oil-Free Mattifying Moisturizer",
                        description = "Lightweight formula that controls shine without clogging pores."
                    )
                )
            }
            "Combination" -> {
                recommendations.add(
                    Recommendation(
                        category = RecommendationType.PRODUCT,
                        title = "Balancing Toner",
                        description = "Helps regulate oil production while maintaining hydration balance."
                    )
                )
            }
            "Sensitive" -> {
                recommendations.add(
                    Recommendation(
                        category = RecommendationType.PRODUCT,
                        title = "Gentle Calming Cream",
                        description = "Fragrance-free formula with soothing ingredients to reduce redness and irritation."
                    )
                )
            }
        }
        
        // Lifestyle recommendations
        recommendations.add(
            Recommendation(
                category = RecommendationType.LIFESTYLE,
                title = "Hydration & Sleep",
                description = "Drink 8 glasses of water daily and aim for 7-8 hours of quality sleep for optimal skin recovery."
            )
        )
        
        recommendations.add(
            Recommendation(
                category = RecommendationType.LIFESTYLE,
                title = "Healthy Diet",
                description = "Include antioxidant-rich foods like berries, leafy greens, and omega-3 fatty acids for skin health."
            )
        )
        
        return recommendations
    }

    fun calculateSkinAge(actualAge: Int, concerns: List<SkinConcern>): Int {
        var ageModifier = 0
        
        concerns.forEach { concern ->
            when (concern.severity) {
                Severity.MILD -> ageModifier += 1
                Severity.MODERATE -> ageModifier += 2
                Severity.SEVERE -> ageModifier += 3
            }
        }
        
        return (actualAge + ageModifier).coerceIn(18, 80)
    }
}
