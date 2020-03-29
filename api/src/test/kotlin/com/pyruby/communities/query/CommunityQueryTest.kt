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
        testClient.post()
            .uri("/graphql")
            .header("Authorization", HttpHeaders.encodeBasicAuth(expectedUser, "foo", null))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType("application", "graphql"))
            .bodyValue(""" query {
  community {
    id
    name
    households(paging: {first: 10}) {
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
}""")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("${"$"}.data.community.name").isEqualTo("Deans Farm")
            .jsonPath("${"$"}.data.community.households.edges.length()").isEqualTo(2)
            .jsonPath("${"$"}.data.community.households.edges[0].node.members.edges[0].node.userId").isEqualTo("chris_t")
    }

}
