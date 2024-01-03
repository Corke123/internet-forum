package org.unibl.etf.sni.authorizationserver.config.keys

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component

@Component
class RsaKeyPairRepositoryJWKSource(val keyPairRepository: RsaKeyPairRepository) : JWKSource<SecurityContext>,
    OAuth2TokenCustomizer<JwtEncodingContext> {
    override fun get(jwkSelector: JWKSelector?, context: SecurityContext?): MutableList<JWK> =
        keyPairRepository.findKeyPairs()
            .map { RSAKey.Builder(it.publicKey).privateKey(it.privateKey).keyID(it.id).build() }
            .filter { jwkSelector?.matcher?.matches(it) ?: false }
            .map { it as JWK }
            .toMutableList()


    override fun customize(context: JwtEncodingContext?) {
        val keyPairs = keyPairRepository.findKeyPairs()
        val kid = keyPairs.first().id
        context?.jwsHeader?.keyId(kid)

        if (context?.tokenType == OAuth2TokenType.ACCESS_TOKEN) {
            val principal = context?.getPrincipal<UsernamePasswordAuthenticationToken>()
            val authorities = principal?.authorities?.map { it.authority }
            val roles = authorities?.filter { it.startsWith("ROLE_") }
            val permissions = authorities?.filter { it.matches(Regex("^comment:(?:write|edit|delete)\$")) }
            context?.claims?.claims {
                it["roles"] = roles
                it["permissions"] = permissions
            }
        }
    }
}
