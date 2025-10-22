package com.example.nihongomaster.data.remote.auth

import com.example.nihongomaster.data.local.dao.TokenDao
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking

/**
 * Interceptor to add Authorization header when access token available in DB.
 * Uses blocking call to TokenDao (runBlocking) because OkHttp interceptors are synchronous.
 */
class AuthInterceptor(private val tokenDao: TokenDao) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = runBlocking { tokenDao.getTokens() }
        return if (token != null) {
            val req = original.newBuilder()
                .header("Authorization", "Bearer ${token.accessToken}")
                .build()
            chain.proceed(req)
        } else {
            chain.proceed(original)
        }
    }
}
