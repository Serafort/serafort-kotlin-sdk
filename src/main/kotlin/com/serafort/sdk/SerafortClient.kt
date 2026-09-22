package com.serafort.sdk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class SerafortClient(
    val config: SerafortConfig,
    private val storage: SecureStorage = InMemorySecureStorage(),
    private val httpClient: OkHttpClient = OkHttpClient()
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val _currentUser = MutableStateFlow<UserContext?>(null)
    val currentUser: StateFlow<UserContext?> = _currentUser.asStateFlow()

    suspend fun validateToken(token: String): UserContext = withContext(Dispatchers.IO) {
        val url = "${config.endpoint.trimEnd('/')}/api/v1/auth/me"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Accept", "application/json")
            .build()

        try {
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw SerafortException.AuthenticationFailedException(
                        "Server returned HTTP ${response.code}: ${response.message}"
                    )
                }

                val body = response.body?.string()
                    ?: throw SerafortException.AuthenticationFailedException("Empty response body")

                val user = json.decodeFromString<UserContext>(body)
                _currentUser.value = user
                user
            }
        } catch (e: Exception) {
            if (e is SerafortException) throw e
            throw SerafortException.NetworkException("Failed to validate token", e)
        }
    }

    fun setTokens(tokens: AuthTokens) {
        storage.save("${config.storageKeyPrefix}_access_token", tokens.accessToken)
        tokens.refreshToken?.let {
            storage.save("${config.storageKeyPrefix}_refresh_token", it)
        }
        tokens.idToken?.let {
            storage.save("${config.storageKeyPrefix}_id_token", it)
        }
    }

    fun getAccessToken(): String? {
        return storage.read("${config.storageKeyPrefix}_access_token")
    }

    fun logout() {
        storage.clear()
        _currentUser.value = null
    }

    fun hasPermission(permission: String): Boolean {
        val user = _currentUser.value ?: return false
        return RBAC.hasPermission(user, permission)
    }

    fun hasRole(role: String): Boolean {
        val user = _currentUser.value ?: return false
        return RBAC.hasRole(user, role)
    }
}
