package com.pyruby.communities

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.pyruby.communities.resolvers.RelaySchemaHook
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    @Bean
    fun wiringFactory() = KotlinDirectiveWiringFactory()

    @Bean
    fun googleTokenVerifier(@Value("${"$"}{client_id}") clientId: String) =
        GoogleIdTokenVerifier.Builder(NetHttpTransport(), JacksonFactory())
            .setAudience(listOf(clientId))
            .build()

    @Bean
    fun hooks(wiringFactory: KotlinDirectiveWiringFactory) = RelaySchemaHook(wiringFactory)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
