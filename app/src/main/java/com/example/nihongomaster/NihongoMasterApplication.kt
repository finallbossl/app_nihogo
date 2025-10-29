package com.example.nihongomaster

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.example.nihongomaster.utils.PerformanceMonitor
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NihongoMasterApplication : Application() {
    private val TAG = "NihongoMasterApp"
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        
        // Enable StrictMode for debugging
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        
        // Start ANR monitoring
        PerformanceMonitor.startAnrWatchdog()
        
        // Initialize Firebase on background thread
        applicationScope.launch(Dispatchers.IO) {
            try {
                FirebaseApp.initializeApp(this@NihongoMasterApplication)
                Log.d(TAG, "Firebase initialized")
            } catch (e: Exception) {
                Log.e(TAG, "Firebase init failed: ${e.message}")
            }
        }
        
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Log.e(TAG, "Uncaught exception: ${exception.message}")
        }
    }
}