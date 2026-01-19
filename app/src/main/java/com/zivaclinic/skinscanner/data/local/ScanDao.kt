package com.zivaclinic.skinscanner.data.local

import androidx.room.*
import com.zivaclinic.skinscanner.data.local.entities.ScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Query("SELECT * FROM scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scans WHERE id = :id")
    suspend fun getScanById(id: String): ScanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanEntity)

    @Delete
    suspend fun deleteScan(scan: ScanEntity)

    @Query("DELETE FROM scans")
    suspend fun deleteAllScans()
}
