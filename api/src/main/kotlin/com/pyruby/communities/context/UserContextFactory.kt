package com.pyruby.communities.context

import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class UserContextFactory(private val tokenVerifier: GoogleIdTokenVerifier) : GraphQLContextFactory<UserContext> {
    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse): UserContext =
        UserContext(extractUser(request.headers.getFirst(HttpHeaders.AUTHORIZATION)!!))

    private fun extractUser(authToken: String) =
        if (authToken.startsWith("Basic"))
            String(Base64.getDecoder().decode(authToken.substringAfter("Basic "))).substringBefore(":")
        else
            tokenVerifier.verify(authToken.substringAfter("Bearer "))!!.payload["name"].toString()
}

data class UserContext(val username: String)
