package com.serafort.sdk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserContext(
    @SerialName("user_id") val userId: String,
    @SerialName("tenant_id") val tenantId: String,
    val roles: List<String> = emptyList(),
    val permissions: List<String> = emptyList(),
    @SerialName("custom_claims") val customClaims: Map<String, String>? = null
)

@Serializable
data class AuthTokens(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("id_token") val idToken: String? = null,
    @SerialName("expires_in") val expiresIn: Int? = null
)

data class SerafortConfig(
    val endpoint: String,
    val clientId: String? = null,
    val redirectUri: String? = null,
    val storageKeyPrefix: String = "serafort_auth"
)

sealed class SerafortException(message: String, cause: Throwable? = null) : RuntimeException(message, cause) {
    class InvalidTokenException(message: String) : SerafortException(message)
    class AuthenticationFailedException(message: String, cause: Throwable? = null) : SerafortException(message, cause)
    class PermissionDeniedException(permission: String) : SerafortException("Missing required permission: $permission")
    class NetworkException(message: String, cause: Throwable? = null) : SerafortException(message, cause)
}
