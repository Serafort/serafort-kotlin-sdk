package com.serafort.sdk

import java.util.concurrent.ConcurrentHashMap

interface SecureStorage {
    fun save(key: String, value: String)
    fun read(key: String): String?
    fun delete(key: String)
    fun clear()
}

/**
 * In-memory fallback storage used in testing or non-Android JVM environments.
 */
class InMemorySecureStorage : SecureStorage {
    private val map = ConcurrentHashMap<String, String>()

    override fun save(key: String, value: String) {
        map[key] = value
    }

    override fun read(key: String): String? {
        return map[key]
    }

    override fun delete(key: String) {
        map.remove(key)
    }

    override fun clear() {
        map.clear()
    }
}
