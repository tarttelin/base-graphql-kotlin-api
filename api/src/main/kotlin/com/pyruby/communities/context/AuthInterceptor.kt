package com.pyruby.communities.context

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.Base64

@Order(-3)
@Component
class AuthInterceptor(private val tokenVerifier: GoogleIdTokenVerifier) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]
        return if (hasValidAuthHeader(authHeader)) {
            chain.filter(exchange)
        } else {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            Mono.empty()
        }
    }

    private fun hasValidAuthHeader(authHeader: MutableList<String>?): Boolean =
        if (authHeader?.any { it.startsWith("Basic ") } == true) {
            val token = authHeader[0]
            val decodedToken = String(Base64.getDecoder().decode(token.substringAfter("Basic ")))
            decodedToken.length > 2 && decodedToken.contains(":")
        } else if (authHeader?.any { it.startsWith("Bearer ") } == true) {
            val token = authHeader[0]
            val jwt = token.substringAfter("Bearer ")
            tokenVerifier.verify(jwt) != null
        } else {
            false
        }
}
