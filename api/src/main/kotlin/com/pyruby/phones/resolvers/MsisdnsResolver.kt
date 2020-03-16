package com.pyruby.phones.resolvers

import com.pyruby.phones.MsisdnRepository
import com.pyruby.phones.model.Customer
import com.pyruby.phones.model.Msisdn
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component("msisdnsResolver")
class MsisdnsResolver(private val msisdnRepo: MsisdnRepository) : DataFetcher<List<Msisdn>> {

    override fun get(environment: DataFetchingEnvironment): List<Msisdn> {
        val customerId = environment.getSource<Customer>().id
        val isPrimary: Boolean? = environment.getArgument("primary")
        return msisdnRepo.findByCustomerId(customerId, isPrimary ?: false)
    }
}
