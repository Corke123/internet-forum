package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.util.FileCopyUtils
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

class RsaPrivateKeyConverter(private val textEncryptor: TextEncryptor) : Serializer<RSAPrivateKey>,
    Deserializer<RSAPrivateKey> {
    override fun serialize(rsaPrivateKey: RSAPrivateKey, outputStream: OutputStream) {
        val pkcS8EncodedKeySpec = PKCS8EncodedKeySpec(rsaPrivateKey.encoded)
        val privateKey = """
            -----BEGIN PRIVATE KEY-----
            ${Base64.getMimeEncoder().encodeToString(pkcS8EncodedKeySpec.encoded)}
            -----END PRIVATE KEY-----
        """.trimIndent()
        outputStream.write(textEncryptor.encrypt(privateKey).toByteArray())
    }

    override fun deserialize(inputStream: InputStream): RSAPrivateKey {
        val pem = textEncryptor.decrypt(FileCopyUtils.copyToString(InputStreamReader(inputStream)))
        val privateKeyPem = pem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
        val encoded = Base64.getMimeDecoder().decode(privateKeyPem)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(encoded)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

}
