package com.zivaclinic.skinscanner.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val overallScore: Int,
    val skinAge: Int,
    val hydration: Int,
    val elasticity: Int,
    val skinType: String,
    val imagePaths: String, // JSON array as string
    val concernsJson: String,
    val recommendationsJson: String
)
