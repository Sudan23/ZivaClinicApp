package com.zivaclinic.skinscanner.data.repository

import com.google.gson.Gson
import com.zivaclinic.skinscanner.data.local.ScanDao
import com.zivaclinic.skinscanner.data.local.entities.ScanEntity
import com.zivaclinic.skinscanner.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScanRepository(private val scanDao: ScanDao) {
    private val gson = Gson()

    fun getAllScans(): Flow<List<ScanResult>> {
        return scanDao.getAllScans().map { entities ->
            entities.map { entityToScanResult(it) }
        }
    }

    suspend fun getScanById(id: String): ScanResult? {
        return scanDao.getScanById(id)?.let { entityToScanResult(it) }
    }

    suspend fun insertScan(scanResult: ScanResult) {
        scanDao.insertScan(scanResultToEntity(scanResult))
    }

    suspend fun deleteScan(scanResult: ScanResult) {
        scanDao.deleteScan(scanResultToEntity(scanResult))
    }

    private fun scanResultToEntity(scanResult: ScanResult): ScanEntity {
        return ScanEntity(
            id = scanResult.id,
            timestamp = scanResult.timestamp,
            overallScore = scanResult.overallScore,
            skinAge = scanResult.skinAge,
            hydration = scanResult.hydration,
            elasticity = scanResult.elasticity,
            skinType = scanResult.skinType,
            imagePaths = gson.toJson(scanResult.imagePaths),
            concernsJson = gson.toJson(scanResult.concerns),
            recommendationsJson = gson.toJson(scanResult.recommendations)
        )
    }

    private fun entityToScanResult(entity: ScanEntity): ScanResult {
        return ScanResult(
            id = entity.id,
            timestamp = entity.timestamp,
            overallScore = entity.overallScore,
            skinAge = entity.skinAge,
            hydration = entity.hydration,
            elasticity = entity.elasticity,
            skinType = entity.skinType,
            imagePaths = gson.fromJson(entity.imagePaths, Array<String>::class.java).toList(),
            concerns = gson.fromJson(entity.concernsJson, Array<SkinConcern>::class.java).toList(),
            recommendations = gson.fromJson(entity.recommendationsJson, Array<Recommendation>::class.java).toList()
        )
    }
}
