package org.unibl.etf.sni.accesscontroller.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http.authorizeExchange { it.anyExchange().authenticated() }
            .csrf { it.disable() }
            .oauth2Login { Customizer.withDefaults<ServerHttpSecurity.OAuth2LoginSpec>() }
            .oauth2Client { Customizer.withDefaults<ServerHttpSecurity.OAuth2ClientSpec>() }.build()

}