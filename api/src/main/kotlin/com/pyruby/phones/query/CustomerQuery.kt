package com.pyruby.phones.query

import com.expediagroup.graphql.annotations.GraphQLContext
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import com.pyruby.phones.CustomerRepository
import com.pyruby.phones.context.PhoneContext
import com.pyruby.phones.model.Customer
import org.springframework.stereotype.Component

@Component
class CustomerQuery(private val customerRepo: CustomerRepository) : Query {

    @GraphQLDescription("Lookup customer")
    fun customer(@GraphQLContext context: PhoneContext): Customer? {
        return customerRepo.get(context.msisdn!!)
    }
}
