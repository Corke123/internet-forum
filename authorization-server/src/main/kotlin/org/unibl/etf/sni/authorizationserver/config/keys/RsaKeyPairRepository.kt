package org.unibl.etf.sni.authorizationserver.config.keys

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant

interface RsaKeyPairRepository {
    fun findKeyPairs(): List<RsaKeyPair>
    fun save(rsaKeyPair: RsaKeyPair)

    data class RsaKeyPair(
        val id: String,
        val created: Instant,
        val publicKey: RSAPublicKey,
        val privateKey: RSAPrivateKey
    )
}
