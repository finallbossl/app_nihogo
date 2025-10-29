package com.example.nihongomaster.data.remote.auth

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlinx.coroutines.runBlocking

/**
 * Authenticator that calls a refreshToken function when server returns 401.
 * refreshToken should perform a network refresh and persist new tokens.
 * It must be safe to call from a blocking context.
 */
class TokenAuthenticator(
    private val refreshTokenBlocking: () -> Boolean,
    private val getAccessTokenBlocking: () -> String?
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // Prevent infinite loops
        if (responseCount(response) >= 2) return null

        val refreshed = try {
            runBlocking { refreshTokenBlocking() }
        } catch (e: Exception) {
            false
        }

        return if (refreshed) {
            val newToken = runBlocking { getAccessTokenBlocking() }
            newToken?.let {
                response.request.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            }
        } else null
    }

    private fun responseCount(response: Response): Int {
        var res: Response? = response
        var result = 1
        while (res?.priorResponse != null) {
            result++
            res = res.priorResponse
        }
        return result
    }
}
