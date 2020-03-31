package com.pyruby.communities.query

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
class CommunityQueryTest(@Autowired private val testClient: WebTestClient) {

    @Test
    fun `Find community for logged in user`() {
        val expectedUser = "chris_t"
        val query = """ query {
  community {
    id
    name
    households(first: 10) {
      edges {
        node {
          address {
          	nameOrNumber
            postcode
          }
          id
          members {
            edges {
              node {
                id
                name {
                  preferredName
                }
                userId
                things {
                  edges {
                    node {
                      name
                      category
                      quantity
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}"""
        execute(query, expectedUser)
            .jsonPath("${"$"}.data.community.name").isEqualTo("Deans Farm")
            .jsonPath("${"$"}.data.community.households.edges.length()").isEqualTo(2)
            .jsonPath("${"$"}.data.community.households.edges[0].node.members.edges[0].node.userId").isEqualTo("chris_t")
    }

    @Test
    fun `find household by userId`() {
        val query = "query { household { address { postcode } } }"
        execute(query, "shipsmouse")
            .jsonPath("${"$"}.data.household.address.postcode").isEqualTo("RG4 5JZ")
    }

    @Test
    fun `find member by userId`() {
        val query = "query { member { name { preferredName }, household { address { postcode } }, things { edges { node { name } } } } }"
        execute(query, "chris_t")
            .jsonPath("${"$"}.data.member.household.address.postcode").isEqualTo("RG4 5JZ")
            .jsonPath("${"$"}.data.member.things.edges[0].node.name").isEqualTo("Strong plain bread flour")
    }

    @Test
    fun `find things by userId`() {
        val query = "query { things { name } }"
        execute(query, "mary-reed")
            .jsonPath("${"$"}.data.things[0].name").isEqualTo("Cheese")
    }

    @Test
    fun `find things by householdId`() {
        val query = """query { thingsForHousehold(householdId: "2") { edges { node { name } } } }"""
        execute(query, "mary-reed")
            .jsonPath("${"$"}.data.thingsForHousehold.edges[0].node.name").isEqualTo("Cheese")
    }

    @Test
    fun `query without a logged in user returns an unauthorized exception`() {
        testClient.post()
            .uri("/graphql")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue("query { community { name } }")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `query with an invalid auth token returns unauthorized exception`() {
        testClient.post()
            .uri("/graphql")
            .header("Authorization", "Reubens")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue("query { community { name } }")
            .exchange()
            .expectStatus().isUnauthorized
    }

    private fun execute(query: String, userId: String = "chris_t") =
        testClient.post()
            .uri("/graphql")
            .header("Authorization", "Basic " + HttpHeaders.encodeBasicAuth(userId, "foo", null))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue(query)
            .exchange()
            .expectStatus().isOk
            .expectBody()
}
