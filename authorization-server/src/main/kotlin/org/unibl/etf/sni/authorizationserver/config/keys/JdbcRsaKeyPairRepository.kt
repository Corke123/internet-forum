package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.unibl.etf.sni.authorizationserver.config.keys.RsaKeyPairRepository.RsaKeyPair
import java.io.ByteArrayOutputStream
import java.util.*


@Component
class JdbcRsaKeyPairRepository(
    val jdbcTemplate: JdbcTemplate,
    val rsaPublicKeyConverter: RsaPublicKeyConverter,
    val rsaPrivateKeyConverter: RsaPrivateKeyConverter,
    val rowMapper: RowMapper<RsaKeyPair>
) : RsaKeyPairRepository {
    override fun findKeyPairs(): List<RsaKeyPair> =
        jdbcTemplate.query("select * from rsa_key_pairs order by created desc", rowMapper)

    override fun save(rsaKeyPair: RsaKeyPair) {
        val sql = """
                insert into rsa_key_pairs (id, private_key, public_key, created) values (?, ?, ?, ?)
                on conflict on constraint rsa_key_pairs_id_created_key do nothing
                """.trimIndent()
        ByteArrayOutputStream().use { privateBaos ->
            ByteArrayOutputStream().use { publicBaos ->
                rsaPrivateKeyConverter.serialize(rsaKeyPair.privateKey, privateBaos)
                rsaPublicKeyConverter.serialize(rsaKeyPair.publicKey, publicBaos)
                val updated = jdbcTemplate.update(
                    sql,
                    rsaKeyPair.id,
                    privateBaos.toString(),
                    publicBaos.toString(),
                    Date(rsaKeyPair.created.toEpochMilli())
                )
                Assert.state(updated == 0 || updated == 1, "no more than one record should have been updated")
            }
        }
    }


}
