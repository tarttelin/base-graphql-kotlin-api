package com.pyruby.phones.query

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerQueryTest(@Autowired private val testClient: WebTestClient) {

    @Test
    fun `Find customer with only primary msisdns using msisdn in header`() {
        val expectedMsisdn = "123123123"
        testClient.post()
            .uri("/graphql")
            .header("msisdn", expectedMsisdn)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue("query { customer { id, name { firstName, lastName }, msisdns(primary: true) { value, duration } } }")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("${"$"}.data.customer.id").isEqualTo(expectedMsisdn)
    }

    @Test
    fun `Find customer with unfiltered msisdns using msisdn in header`() {
        val expectedMsisdn = "123123123"
        testClient.post()
            .uri("/graphql")
            .header("msisdn", expectedMsisdn)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue("query { customer { id, name { firstName }, msisdns { value, duration } } }")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("${"$"}.data.customer.id").isEqualTo(expectedMsisdn)
    }
}
