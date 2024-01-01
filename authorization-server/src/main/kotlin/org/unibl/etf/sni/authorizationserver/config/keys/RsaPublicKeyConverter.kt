package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.util.FileCopyUtils
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

class RsaPublicKeyConverter(private val textEncryptor: TextEncryptor) : Serializer<RSAPublicKey>, Deserializer<RSAPublicKey> {
    override fun serialize(publicKey: RSAPublicKey, outputStream: OutputStream) {
        val x509EncodedKeySpec = X509EncodedKeySpec(publicKey.encoded)
        val pem = """
            -----BEGIN PUBLIC KEY-----
            ${Base64.getMimeEncoder().encodeToString(x509EncodedKeySpec.encoded)}
            -----END PUBLIC KEY-----
        """.trimIndent()
        outputStream.write(textEncryptor.encrypt(pem).toByteArray())
    }

    override fun deserialize(inputStream: InputStream): RSAPublicKey {
        val pem = textEncryptor.decrypt(FileCopyUtils.copyToString(InputStreamReader(inputStream)))
        val publicKeyPem = pem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
        val encoded = Base64.getMimeDecoder().decode(publicKeyPem)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(encoded)
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }
}
