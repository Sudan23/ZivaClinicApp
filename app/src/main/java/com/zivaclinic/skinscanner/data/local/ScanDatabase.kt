package com.zivaclinic.skinscanner.data.local

import android.content.Context
import androidx.room.*
import com.zivaclinic.skinscanner.data.local.entities.ScanEntity

@Database(entities = [ScanEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ScanDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao

    companion object {
        @Volatile
        private var INSTANCE: ScanDatabase? = null

        fun getDatabase(context: Context): ScanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScanDatabase::class.java,
                    "ziva_scan_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
