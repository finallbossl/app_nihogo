package com.example.nihongomaster

import android.app.Application
import android.os.StrictMode

class NihongoMasterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Disable strict mode in debug builds to prevent ANR
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
        StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
    }
}