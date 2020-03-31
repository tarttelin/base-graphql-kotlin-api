package com.pyruby.communities.mutation

import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommunityMutationTest(@Autowired private val testClient: WebTestClient) {

    @Test
    fun `create a new community`() {
        val userId = "rob_dooly"
        val mutation = """mutation { createCommunity(community: {name: "Nowheresville", preferredName: "Bob the Gob", houseNameOrNumber: "12", postcode: "NW10 3KT"}) {
|name, id, households { edges { node { address {postcode}, members { edges { node { userId, name { preferredName } } } } } } } } }""".trimMargin()
        testClient.post()
            .uri("/graphql")
            .header("Authorization", "Basic " + HttpHeaders.encodeBasicAuth(userId, "foo", null))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue(mutation)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("${"$"}.data.createCommunity.name").isEqualTo("Nowheresville")
            .jsonPath("${"$"}.data.createCommunity.households.edges[0].node.address.postcode").isEqualTo("NW10 3KT")
            .jsonPath("${"$"}.data.createCommunity.households.edges[0].node.members.edges[0].node.userId").isEqualTo("rob_dooly")
            .jsonPath("${"$"}.data.createCommunity.households.edges[0].node.members.edges[0].node.name.preferredName").isEqualTo("Bob the Gob")
    }
}
