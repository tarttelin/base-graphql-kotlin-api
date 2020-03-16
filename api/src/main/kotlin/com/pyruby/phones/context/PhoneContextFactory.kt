package com.pyruby.phones.context

import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component

@Component
class PhoneContextFactory : GraphQLContextFactory<PhoneContext> {
    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse): PhoneContext =
        PhoneContext(request.headers.getFirst("msisdn"))
}

data class PhoneContext(val msisdn: String?)
