package com.pyruby.communities

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.pyruby.communities.resolvers.RelaySchemaHook
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    @Bean
    fun wiringFactory() = KotlinDirectiveWiringFactory()

    @Bean
    fun hooks(wiringFactory: KotlinDirectiveWiringFactory) =  RelaySchemaHook(wiringFactory)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
