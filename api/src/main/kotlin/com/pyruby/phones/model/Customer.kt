package com.pyruby.phones.model

import com.expediagroup.graphql.annotations.GraphQLID
import com.pyruby.phones.resolvers.MsisdnsResolver
import com.pyruby.phones.resolvers.UseResolver

data class Customer(@GraphQLID val id: String, val name: CustomerName) {

    @Suppress("UNUSED_PARAMETER")
    @UseResolver<MsisdnsResolver>(MsisdnsResolver::class)
    fun msisdns(primary: Boolean?): List<Msisdn> {
        throw resolverException(this::msisdns)
    }
}

data class CustomerName(val firstName: String, val lastName: String)

data class Msisdn(val value: String, val duration: String)
