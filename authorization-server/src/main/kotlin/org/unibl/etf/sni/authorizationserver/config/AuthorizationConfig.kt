package org.unibl.etf.sni.authorizationserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository

@Configuration
class AuthorizationConfig {

    @Bean
    fun jdbcOauth2AuthorizationConsentService(
        jdbcOperations: JdbcOperations,
        registeredClientRepository: RegisteredClientRepository
    ) = JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository)

    @Bean
    fun jdbcOauth2AuthorizationService(
        jdbcOperations: JdbcOperations,
        registeredClientRepository: RegisteredClientRepository
    ) = JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository)
}
