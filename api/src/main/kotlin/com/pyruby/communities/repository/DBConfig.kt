package com.pyruby.communities.repository

import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class DBConfig : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory() = H2ConnectionFactory.inMemory("Bob")

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory) = R2dbcTransactionManager(connectionFactory)

    @Bean
    fun initializer(connectionFactory: ConnectionFactory) : ConnectionFactoryInitializer {
        val populator = CompositeDatabasePopulator()
            .apply {
                addPopulators(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
                addPopulators(ResourceDatabasePopulator(ClassPathResource("data.sql")))
            }
        return ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(populator)
        }
    }
}
