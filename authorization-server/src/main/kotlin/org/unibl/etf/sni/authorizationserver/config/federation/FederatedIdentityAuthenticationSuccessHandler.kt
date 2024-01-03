package org.unibl.etf.sni.authorizationserver.config.federation

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import java.util.function.Consumer


class FederatedIdentityAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    private val delegate: AuthenticationSuccessHandler = SavedRequestAwareAuthenticationSuccessHandler()

    private val oauth2UserHandler: Consumer<OAuth2User> = Consumer<OAuth2User> { user ->
        println("Oauth2 user logged in")
        println("User info: $user")
    }

    private val oidcUserHandler: Consumer<OidcUser> = Consumer<OidcUser> { user ->
        println("OidcUser user logged in")
        println("User info: $user")
        oauth2UserHandler.accept(user)
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?
    ) {
        if (authentication is OAuth2AuthenticationToken) {
            when (val principal = authentication.principal) {
                is OidcUser -> this.oidcUserHandler.accept(principal)
                is OAuth2User -> this.oauth2UserHandler.accept(principal)
            }
        }

        this.delegate.onAuthenticationSuccess(request, response, authentication)
    }
}