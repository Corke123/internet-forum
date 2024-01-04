package org.unibl.etf.sni.accesscontroller.config

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import reactor.core.publisher.Mono

class LogoutSuccessHandler(oidcLogoutSuccessHandler: ReactiveClientRegistrationRepository) :
    ServerLogoutSuccessHandler {
    private val delegate: OidcClientInitiatedServerLogoutSuccessHandler =
        OidcClientInitiatedServerLogoutSuccessHandler(oidcLogoutSuccessHandler)

    init {
        delegate.setPostLogoutRedirectUri("{baseUrl}")
    }

    override fun onLogoutSuccess(exchange: WebFilterExchange?, authentication: Authentication?): Mono<Void> {
        return delegate.onLogoutSuccess(exchange, authentication).then(Mono.fromRunnable {
            exchange?.exchange?.response?.statusCode = HttpStatus.ACCEPTED
        })
    }
}