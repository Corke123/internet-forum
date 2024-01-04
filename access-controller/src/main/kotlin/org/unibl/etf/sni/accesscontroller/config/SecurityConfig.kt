package org.unibl.etf.sni.accesscontroller.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler

@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        clientRegistrationRepository: ReactiveClientRegistrationRepository
    ): SecurityWebFilterChain =
        http.authorizeExchange {
            it
                .pathMatchers("/static/**", "/logged-out").permitAll()
                .anyExchange().authenticated()
        }
            .csrf { it.disable() }
            .oauth2Login {
                it.authenticationSuccessHandler(RedirectServerAuthenticationSuccessHandler("/static/browser/index.html"))
            }
            .oauth2Client { Customizer.withDefaults<ServerHttpSecurity.OAuth2ClientSpec>() }
            .logout {
                it.logoutSuccessHandler(LogoutSuccessHandler(clientRegistrationRepository))
            }
            .build()

}