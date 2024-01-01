package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import org.unibl.etf.sni.authorizationserver.config.keys.RsaKeyPairRepository.RsaKeyPair
import java.sql.ResultSet
import java.util.Date

@Component
class RsaKeyPairRowMapper(
    val rsaPrivateKeyConverter: RsaPrivateKeyConverter,
    val rsaPublicKeyConverter: RsaPublicKeyConverter
) : RowMapper<RsaKeyPair> {
    override fun mapRow(rs: ResultSet, rowNum: Int): RsaKeyPair? {
        val privateKeyBytes = rs.getString("private_key").toByteArray()
        val privateKey = rsaPrivateKeyConverter.deserializeFromByteArray(privateKeyBytes)

        val publicKeyBytes = rs.getString("public_key").toByteArray()
        val publicKey = rsaPublicKeyConverter.deserializeFromByteArray(publicKeyBytes)

        val created = Date(rs.getDate("created").time).toInstant()
        val id = rs.getString("id")

        return RsaKeyPair(id, created, publicKey, privateKey)
    }
}
