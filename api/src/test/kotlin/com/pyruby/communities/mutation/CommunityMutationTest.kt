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
        execute(userId, mutation)
            .jsonPath("${"$"}.data.createCommunity.name").isEqualTo("Nowheresville")
            .jsonPath("${"$"}.data.createCommunity.households.edges[0].node.address.postcode").isEqualTo("NW10 3KT")
            .jsonPath("${"$"}.data.createCommunity.households.edges[0].node.members.edges[0].node.userId").isEqualTo("rob_dooly")
            .jsonPath("${"$"}.data.createCommunity.households.edges[0].node.members.edges[0].node.name.preferredName").isEqualTo("Bob the Gob")
    }

    @Test
    fun `Join a community`() {
        val userId = "robbie"
        val mutation = """mutation { joinCommunity(request: { communityId: "1", preferredName: "Shandie", houseNameOrNumber: "2", postcode: "RG4 5JZ" }) {
|userId, name { preferredName } } }""".trimMargin()
        execute(userId, mutation)
            .jsonPath("${"$"}.data.joinCommunity.name.preferredName").isEqualTo("Shandie")
            .jsonPath("${"$"}.data.joinCommunity.userId").isEqualTo(userId)
    }

    @Test
    fun `join a household`() {
        val userId = "meow"
        val mutation = """mutation { joinHousehold(request: { householdId: "1", preferredName: "Kittencat"}){ userId, name { preferredName } } }"""
        execute(userId, mutation)
            .jsonPath("${"$"}.data.joinHousehold.name.preferredName").isEqualTo("Kittencat")
            .jsonPath("${"$"}.data.joinHousehold.userId").isEqualTo(userId)
    }

    @Test
    fun `add thing to my list`() {
        val userId = "chris_t"
        val mutation = """ mutation { addThing(request: { name: "Kale", quantity: "Big bunch", category: Groceries }) { things { edges { node { name, quantity, category } } } } }"""
        execute(userId, mutation)
            .jsonPath("${"$"}.data.addThing.things.edges[-1:].node.name").isEqualTo("Kale")
            .jsonPath("${"$"}.data.addThing.things.edges[-1:].node.quantity").isEqualTo("Big bunch")
            .jsonPath("${"$"}.data.addThing.things.edges[-1:].node.category").isEqualTo("Groceries")
    }

    @Test
    fun `remove thing from my list`() {

        val userId = "chris_t"
        val mutation = """ mutation { addThing(request: { name: "Cheese", quantity: "2", category: Groceries }) { things { edges { node { id } } } } }"""
        execute(userId, mutation)
            .jsonPath("${"$"}..id").value(fun(ids: List<String>) {
                val removeMutation = """ mutation { removeThing(request: { id: "${ids.last()}"}) { things { edges { node { id } } } } }"""
                execute(userId, removeMutation)
                    .jsonPath("${"$"}..edges.length()").isEqualTo(ids.size - 1)
            })
    }

    private fun execute(userId: String, mutation: String): WebTestClient.BodyContentSpec {
        return testClient.post()
            .uri("/graphql")
            .header("Authorization", "Basic " + HttpHeaders.encodeBasicAuth(userId, "foo", null))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue(mutation)
            .exchange()
            .expectStatus().isOk
            .expectBody()
    }
}
