package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.stereotype.Component
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.UUID

@Component
class Keys {

    fun generateKeyPair(created: Instant): RsaKeyPairRepository.RsaKeyPair {
        val keyPair = generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        return RsaKeyPairRepository.RsaKeyPair(
            UUID.randomUUID().toString(), created, publicKey, privateKey
        )
    }

    private fun generateRsaKey(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }
}