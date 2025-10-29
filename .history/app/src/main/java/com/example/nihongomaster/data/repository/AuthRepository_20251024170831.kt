package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.local.dao.TokenDao
import com.example.nihongomaster.data.local.entities.TokenEntity
import com.example.nihongomaster.data.remote.api.NihongoApiService
import com.example.nihongomaster.data.remote.dto.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: NihongoApiService,
    private val tokenDao: TokenDao
) {

    suspend fun register(email: String, password: String, displayName: String? = null): Result<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val req = RegisterRequest(email = email, password = password, displayName = displayName)
                val resp = api.register(req)
                if (resp.isSuccessful) {
                    resp.body()?.let { saveTokens(it); return@withContext Result.success(it) }
                }
                Result.failure(Exception("Register failed: ${resp.code()}"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun login(email: String, password: String): Result<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val req = LoginRequest(email = email, password = password)
                val resp = api.login(req)
                if (resp.isSuccessful) {
                    resp.body()?.let { saveTokens(it); return@withContext Result.success(it) }
                }
                Result.failure(Exception("Login failed: ${resp.code()}"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun refresh(): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val current = tokenDao.getTokens() ?: return@withContext Result.failure(Exception("No refresh token"))
            val req = RefreshRequest(refreshToken = current.refreshToken)
            val resp = api.refresh(req)
            if (resp.isSuccessful) {
                resp.body()?.let { saveTokens(it); return@withContext Result.success(it) }
            }
            Result.failure(Exception("Refresh failed: ${resp.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val current = tokenDao.getTokens() ?: run {
                return@withContext Result.success(Unit)
            }
            val req = LogoutRequest(refreshToken = current.refreshToken)
            val resp = api.logout(req)
            if (resp.isSuccessful) {
                tokenDao.clearTokens()
                return@withContext Result.success(Unit)
            }
            Result.failure(Exception("Logout failed: ${resp.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveTokens(resp: AuthResponse) {
        val expiresAt = System.currentTimeMillis() + (resp.expiresIn * 1000)
        val entity = TokenEntity(
            id = 1,
            accessToken = resp.accessToken,
            refreshToken = resp.refreshToken,
            expiresAt = expiresAt
        )
        tokenDao.insertTokens(entity)
    }

    suspend fun getSavedTokens(): TokenEntity? = withContext(Dispatchers.IO) { tokenDao.getTokens() }
}
