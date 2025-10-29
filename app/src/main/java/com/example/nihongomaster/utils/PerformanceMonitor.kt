package com.example.nihongomaster.utils

import android.os.Handler
import android.os.Looper
import android.util.Log

object PerformanceMonitor {
    private const val TAG = "PerformanceMonitor"
    private const val ANR_TIMEOUT = 5000L
    
    private val mainHandler = Handler(Looper.getMainLooper())
    private var anrWatchdog: Runnable? = null
    
    fun startAnrWatchdog() {
        stopAnrWatchdog()
        anrWatchdog = Runnable {
            Log.w(TAG, "Potential ANR detected - Main thread blocked for ${ANR_TIMEOUT}ms")
        }
        mainHandler.postDelayed(anrWatchdog!!, ANR_TIMEOUT)
    }
    
    fun stopAnrWatchdog() {
        anrWatchdog?.let { mainHandler.removeCallbacks(it) }
        anrWatchdog = null
    }
    
    fun resetAnrWatchdog() {
        stopAnrWatchdog()
        startAnrWatchdog()
    }
}