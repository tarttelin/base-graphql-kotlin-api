package com.pyruby.communities.context

import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Order(-3)
@Component
class AuthInterceptor : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]
        return if (authHeader == null || !authHeader.any { it.startsWith("Basic ") }) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            Mono.empty()
        } else {
            chain.filter(exchange)
        }
    }
}
