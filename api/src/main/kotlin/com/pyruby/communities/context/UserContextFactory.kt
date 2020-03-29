package com.pyruby.communities.context

import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component

@Component
class UserContextFactory : GraphQLContextFactory<UserContext> {
    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse): UserContext =
        UserContext(request.headers.getFirst("msisdn"))
}

data class UserContext(val msisdn: String?)
