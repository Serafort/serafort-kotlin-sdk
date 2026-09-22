# Serafort SDK for Kotlin & Android

Enterprise IAM, EncryptedSharedPreferences secure token storage, Chrome Custom Tabs SSO, and Coroutines/StateFlow for Android and Kotlin applications.

## Features

- 🔒 **Hardware-Backed Encryption**: Android KeyStore and `EncryptedSharedPreferences` integration.
- ⚡ **Kotlin Coroutines & Flow**: Reactive `StateFlow<UserContext?>` for Jetpack Compose or XML-based architectures.
- 🌐 **Enterprise SSO**: PKCE S256 code challenge generation and OAuth2/OIDC Custom Tabs authentication.
- 🛡️ **Wildcard RBAC**: `RBAC.hasPermission(user, "org:*")` evaluation.
- 🏢 **Multi-Tenant Isolation**: Zero-leakage multi-tenant separation.

## Installation

Add Serafort to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.serafort:serafort-kotlin-sdk:0.1.0")
}
```

## Quick Start

```kotlin
import com.serafort.sdk.*

val config = SerafortConfig(
    endpoint = "https://api.serafort.com",
    clientId = "android_client_id"
)

val client = SerafortClient(config)

// Validate Token in Coroutine
lifecycleScope.launch {
    val user = client.validateToken(token)

    // Check Wildcard Permissions
    if (client.hasPermission("billing:manage")) {
        // Show Billing Action
    }
}
```
