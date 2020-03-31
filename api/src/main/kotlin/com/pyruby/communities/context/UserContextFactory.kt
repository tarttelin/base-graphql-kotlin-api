package com.pyruby.communities.context

import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class UserContextFactory : GraphQLContextFactory<UserContext> {
    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse): UserContext =
        UserContext(extractUser(request.headers.getFirst(HttpHeaders.AUTHORIZATION)!!))

    private fun extractUser(authToken: String): String {
        return String(Base64.getDecoder().decode(authToken.substringAfter("Basic "))).substringBefore(":")
    }
}

data class UserContext(val username: String)
