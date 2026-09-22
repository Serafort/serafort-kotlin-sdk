package com.serafort.sdk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SerafortTest {

    @Test
    fun testWildcardPermissions() {
        val user = UserContext(
            userId = "usr_droid_101",
            tenantId = "tenant_google",
            roles = listOf("admin", "developer"),
            permissions = listOf("org:*", "billing:read")
        )

        // Wildcard match
        assertTrue(RBAC.hasPermission(user, "org:users:create"))
        assertTrue(RBAC.hasPermission(user, "org:settings:update"))

        // Exact match
        assertTrue(RBAC.hasPermission(user, "billing:read"))

        // Non-match
        assertFalse(RBAC.hasPermission(user, "billing:write"))
        assertFalse(RBAC.hasPermission(user, "system:admin"))

        // Global wildcard
        val superUser = UserContext(
            userId = "usr_super",
            tenantId = "tenant_google",
            roles = listOf("superadmin"),
            permissions = listOf("*")
        )
        assertTrue(RBAC.hasPermission(superUser, "any:permission:here"))
    }

    @Test
    fun testRolesAndTenants() {
        val user = UserContext(
            userId = "usr_droid_101",
            tenantId = "tenant_google",
            roles = listOf("admin")
        )

        assertTrue(RBAC.hasRole(user, "admin"))
        assertFalse(RBAC.hasRole(user, "viewer"))

        assertTrue(RBAC.hasTenant(user, "tenant_google"))
        assertFalse(RBAC.hasTenant(user, "tenant_other"))
    }

    @Test
    fun testPKCEGeneration() {
        val pkce1 = PKCEPair.generate()
        val pkce2 = PKCEPair.generate()

        assertNotNull(pkce1.codeVerifier)
        assertNotNull(pkce1.codeChallenge)
        assertEquals("S256", pkce1.codeChallengeMethod)

        assertNotEquals(pkce1.codeVerifier, pkce2.codeVerifier)
        assertNotEquals(pkce1.codeChallenge, pkce2.codeChallenge)
    }

    @Test
    fun testSecureStorage() {
        val storage = InMemorySecureStorage()
        storage.save("key1", "secret_token")
        assertEquals("secret_token", storage.read("key1"))

        storage.delete("key1")
        assertNull(storage.read("key1"))
    }
}
