package com.pyruby.communities.context

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

internal class UserContextFactoryTest {
    @Test
    fun `bearer token user extracted`() {
        runBlocking {
            val verifier: GoogleIdTokenVerifier = mockk()
            val request: ServerHttpRequest = mockk()
            val response: ServerHttpResponse = mockk()
            val factory = UserContextFactory(verifier)
            val verifiedToken: GoogleIdToken = mockk()
            val headers = HttpHeaders()
            headers.add("Authorization", "Bearer someToken")
            every { request.headers } returns headers
            every { verifier.verify("someToken") } returns verifiedToken
            val payload = GoogleIdToken.Payload()
            payload["name"] = "Bob spanner"
            every { verifiedToken.payload } returns payload

            val context = factory.generateContext(request, response)

            context.username `should equal` "Bob spanner"
        }
    }
}
