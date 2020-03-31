package com.pyruby.communities.model

import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.pyruby.communities.resolvers.HouseholdMembersResolver
import com.pyruby.communities.resolvers.HouseholdsResolver
import com.pyruby.communities.resolvers.MemberHouseholdResolver
import com.pyruby.communities.resolvers.MemberThingsResolver
import com.pyruby.communities.resolvers.UseResolver
import graphql.relay.Connection
import reactor.core.publisher.Mono

data class Community(@GraphQLID val id: String, val name: String) {

    @Suppress("UNUSED_PARAMETER")
    @UseResolver<HouseholdsResolver>(HouseholdsResolver::class)
    fun households(first: Int?): Connection<Household> {
        throw resolverException(this::households)
    }
}

data class Household(@GraphQLID val id: String, val address: Address) {

    @Suppress("UNUSED_PARAMETER")
    @UseResolver<HouseholdMembersResolver>(HouseholdMembersResolver::class)
    fun members(): Mono<Connection<Member>> {
        throw resolverException(this::members)
    }
}

data class Address(val nameOrNumber: String, val postcode: String)

data class Member(@GraphQLID val id: String, val name: Name, val userId: String, @GraphQLIgnore val householdId: Int) {
    @Suppress("UNUSED_PARAMETER")
    @UseResolver<MemberHouseholdResolver>(MemberHouseholdResolver::class)
    fun household(): Mono<Household> {
        throw resolverException(this::household)
    }

    @Suppress("UNUSED_PARAMETER")
    @UseResolver<MemberThingsResolver>(MemberThingsResolver::class)
    fun things(): Mono<Connection<Thing>> {
        throw resolverException(this::things)
    }
}

data class Name(val preferredName: String)

data class Thing(val name: String, val quantity: String, val category: Category)

enum class Category {
    Groceries, Medicine, Other
}
