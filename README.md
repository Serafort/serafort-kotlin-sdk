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

## Contributing

### Requirements

- JDK 17+
- Gradle (no wrapper is committed yet; use a local Gradle install, or let CI provision one via `gradle/actions/setup-gradle`)

### Git hooks

This repo ships a portable pre-commit hook under `.githooks/pre-commit` that compiles the main and test Kotlin sources (`compileKotlin compileTestKotlin`) before every commit. It is **not** installed automatically — enable it once per clone with:

```bash
git config core.hooksPath .githooks
```

There is no Husky setup here: Husky is an npm-ecosystem tool that hooks into `package.json`/`node_modules`, and this is a Gradle/Kotlin module with no Node.js tooling involved. A plain POSIX shell script wired through `core.hooksPath` is the idiomatic equivalent for a JVM repo and keeps the module's contribution workflow dependency-free of Node.

### CI

Every push and pull request against `main` runs `gradle check` (compile, tests, and any configured verification tasks) on JDK 17 via GitHub Actions (`.github/workflows/ci.yml`).
