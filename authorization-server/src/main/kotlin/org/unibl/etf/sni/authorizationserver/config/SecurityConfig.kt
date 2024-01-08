package org.unibl.etf.sni.authorizationserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.unibl.etf.sni.authorizationserver.config.federation.FederatedIdentityAuthenticationSuccessHandler


private const val loginEndpoint = "/login"

@Configuration
class SecurityConfig {

    @Bean
    fun authenticationProvider(userDetailsService: UserDetailsService): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        return authProvider
    }

    @Bean
    @Order(1)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
        http.exceptionHandling {
            it.defaultAuthenticationEntryPointFor(
                LoginUrlAuthenticationEntryPoint(loginEndpoint),
                MediaTypeRequestMatcher(
                    MediaType.TEXT_HTML
                )
            )
        }.oauth2ResourceServer {
            it.jwt(Customizer.withDefaults())
        }.logout {
            Customizer.withDefaults<Any>()
        }

        return http.build()
    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers("/assets/**", loginEndpoint).permitAll()
                .anyRequest().authenticated()
        }.formLogin {
            it.loginPage(loginEndpoint)
        }.oauth2Login {
            it.loginPage(loginEndpoint)
                .successHandler(authenticationSuccessHandler())
        }
        return http.build()
    }

}

private fun authenticationSuccessHandler() = FederatedIdentityAuthenticationSuccessHandler()

