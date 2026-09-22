package com.serafort.sdk

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

data class PKCEPair(
    val codeVerifier: String,
    val codeChallenge: String,
    val codeChallengeMethod: String = "S256"
) {
    companion object {
        fun generate(): PKCEPair {
            val secureRandom = SecureRandom()
            val code = ByteArray(32)
            secureRandom.nextBytes(code)
            val verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code)

            val bytes = verifier.toByteArray(Charsets.US_ASCII)
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(bytes, 0, bytes.size)
            val digest = messageDigest.digest()
            val challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest)

            return PKCEPair(verifier, challenge)
        }
    }
}
