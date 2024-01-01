package org.unibl.etf.sni.authorizationserver.config.keys

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import java.time.Instant

@Configuration
class KeysConfig {

    @Bean
    fun keyPairGenerationRequestEventApplicationListener(
        keys: Keys,
        repository: RsaKeyPairRepository
    ): ApplicationListener<RsaKeyPairGenerationRequestEvent> {
        return ApplicationListener {
            repository.save(keys.generateKeyPair(it.source))
        }
    }

    @Bean
    fun applicationReadyEventApplicationListener(
        applicationEventPublisher: ApplicationEventPublisher,
        rsaKeyPairRepository: RsaKeyPairRepository
    ): ApplicationListener<ApplicationReadyEvent> {
        return ApplicationListener {
            if (rsaKeyPairRepository.findKeyPairs().isEmpty()) {
                applicationEventPublisher.publishEvent(RsaKeyPairGenerationRequestEvent(Instant.now()))
            }
        }

    }

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>) = NimbusJwtEncoder(jwkSource)

    @Bean
    fun delegationOauth2TokenGenerator(
        jwtEncoder: JwtEncoder,
        oAuth2TokenCustomizer: OAuth2TokenCustomizer<JwtEncodingContext>
    ): OAuth2TokenGenerator<OAuth2Token> {
        val generator = JwtGenerator(jwtEncoder)
        generator.setJwtCustomizer(oAuth2TokenCustomizer)
        return DelegatingOAuth2TokenGenerator(generator, OAuth2AccessTokenGenerator(), OAuth2RefreshTokenGenerator())
    }

    @Bean
    fun textEncryptor(
        @Value("\${jwk.persistence.password}") password: String,
        @Value("\${jwk.persistence.salt}") salt: String
    ): TextEncryptor = Encryptors.text(password, salt)

}
