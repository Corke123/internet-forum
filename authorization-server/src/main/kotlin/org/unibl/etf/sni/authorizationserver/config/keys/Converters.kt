package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.TextEncryptor

@Configuration
class Converters {

    @Bean
    fun rsaPublicKeyConverter(textEncryptor: TextEncryptor) = RsaPublicKeyConverter(textEncryptor)

    @Bean
    fun rsaPrivateKeyConverter(textEncryptor: TextEncryptor) = RsaPrivateKeyConverter(textEncryptor)
}
