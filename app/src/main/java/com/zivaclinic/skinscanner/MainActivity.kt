package com.zivaclinic.skinscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.zivaclinic.skinscanner.data.local.ScanDatabase
import com.zivaclinic.skinscanner.ui.navigation.MainScreen
import com.zivaclinic.skinscanner.ui.theme.ZivaClinicTheme

class MainActivity : ComponentActivity() {
    
    private lateinit var scanDatabase: ScanDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        scanDatabase = ScanDatabase.getDatabase(applicationContext)
        
        enableEdgeToEdge()
        
        setContent {
            ZivaClinicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainScreen(
                        navController = navController,
                        scanDatabase = scanDatabase
                    )
                }
            }
        }
    }
}
