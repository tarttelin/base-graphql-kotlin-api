package com.pyruby.communities.context

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.Base64
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AuthInterceptorTest {
    private val verifier: GoogleIdTokenVerifier = mockk()
    private val exchange: ServerWebExchange = mockk()
    private val chain: WebFilterChain = mockk()
    private val interceptor = AuthInterceptor(verifier)

    @Test
    fun `basic auth token is valid given it is base64 encoded and contains a semi-colon`() {
        val request: ServerHttpRequest = mockk()
        every { exchange.request } returns request
        val userPass = "some_user:password"
        val token = "Basic " + String(Base64.getEncoder().encode(userPass.toByteArray()))
        val headers = HttpHeaders()
        headers.add("Authorization", token)
        every { request.headers } returns headers
        every { chain.filter(exchange) } returns Mono.empty()

        interceptor.filter(exchange, chain)

        verify { chain.filter(exchange) }
    }

    @Test
    fun `basic auth token is invalid given it is base64 encoded but does not contain a semi-colon`() {
        val request: ServerHttpRequest = mockk()
        val response: ServerHttpResponse = mockk()
        every { exchange.request } returns request
        every { exchange.response } returns response
        val userPass = "some_userpassword"
        val token = "Basic " + String(Base64.getEncoder().encode(userPass.toByteArray()))
        val headers = HttpHeaders()
        headers.add("Authorization", token)
        every { request.headers } returns headers
        every { response.setStatusCode(HttpStatus.UNAUTHORIZED) } returns true

        interceptor.filter(exchange, chain)

        verify { response.statusCode = HttpStatus.UNAUTHORIZED }
    }

    @Test
    fun `basic auth token is invalid given it is too short`() {
        val request: ServerHttpRequest = mockk()
        val response: ServerHttpResponse = mockk()
        every { exchange.request } returns request
        every { exchange.response } returns response
        val userPass = ":"
        val token = "Basic " + String(Base64.getEncoder().encode(userPass.toByteArray()))
        val headers = HttpHeaders()
        headers.add("Authorization", token)
        every { request.headers } returns headers
        every { response.setStatusCode(HttpStatus.UNAUTHORIZED) } returns true

        interceptor.filter(exchange, chain)

        verify { response.statusCode = HttpStatus.UNAUTHORIZED }
    }

    @Test
    fun `return a 401 given no auth token`() {
        val request: ServerHttpRequest = mockk()
        val response: ServerHttpResponse = mockk()
        every { exchange.request } returns request
        every { exchange.response } returns response
        val headers = HttpHeaders()
        every { request.headers } returns headers
        every { response.setStatusCode(HttpStatus.UNAUTHORIZED) } returns true

        interceptor.filter(exchange, chain)

        verify { response.statusCode = HttpStatus.UNAUTHORIZED }
    }

    @Test
    fun `bearer auth token is valid given google verifier returns a validated id token`() {
        val request: ServerHttpRequest = mockk()
        val verifiedToken: GoogleIdToken = mockk()
        every { exchange.request } returns request
        val token = "Bearer someToken"
        val headers = HttpHeaders()
        headers.add("Authorization", token)
        every { verifier.verify(token.substringAfter("Bearer ")) } returns verifiedToken
        every { request.headers } returns headers
        every { chain.filter(exchange) } returns Mono.empty()

        interceptor.filter(exchange, chain)

        verify { chain.filter(exchange) }
    }

    @Test
    fun `bearer auth token is invalid given it is google verifier returns null`() {
        val request: ServerHttpRequest = mockk()
        val response: ServerHttpResponse = mockk()
        every { exchange.request } returns request
        val token = "Bearer someToken"
        val headers = HttpHeaders()
        headers.add("Authorization", token)
        every { verifier.verify(token.substringAfter("Bearer ")) } returns null
        every { request.headers } returns headers
        every { exchange.response } returns response
        every { response.setStatusCode(HttpStatus.UNAUTHORIZED) } returns true

        interceptor.filter(exchange, chain)

        verify { response.statusCode = HttpStatus.UNAUTHORIZED }
    }
}
