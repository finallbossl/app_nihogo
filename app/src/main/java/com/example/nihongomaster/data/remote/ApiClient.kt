package com.example.nihongomaster.data.remote

import com.example.nihongomaster.data.remote.api.ApiService
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Emulator: 10.0.2.2, Device: IP máy tính
    private const val BASE_URL = "http://10.0.2.2:3001/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply { 
        level = HttpLoggingInterceptor.Level.BODY 
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val request =   originalRequest.newBuilder()
                .addHeader("X-Admin-Key", "nihongo-admin-2024")
                .build()

            android.util.Log.d("ApiClient", "Request: ${request.method} ${request.url}")
            try {
                val response = chain.proceed(request)
                android.util.Log.d("ApiClient", "Response: ${response.code} ${response.message}")
                response
            } catch (e: Exception) {
                android.util.Log.e("ApiClient", "Network error: ${e.message}")
                throw e
            }            
        }
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}