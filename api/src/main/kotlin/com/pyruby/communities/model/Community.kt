package com.pyruby.communities.model

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.annotations.GraphQLID
import com.expediagroup.graphql.annotations.GraphQLIgnore
import com.pyruby.communities.resolvers.HouseholdMembersResolver
import com.pyruby.communities.resolvers.HouseholdsResolver
import com.pyruby.communities.resolvers.MemberHouseholdResolver
import com.pyruby.communities.resolvers.MemberThingsResolver
import com.pyruby.communities.resolvers.UseResolver
import graphql.relay.Connection
import reactor.core.publisher.Mono

interface Node {
    @GraphQLID val id: String?

}

@GraphQLDescription("A group of households in the same area who look out for each other")
data class Community(@GraphQLID override val id: String, val name: String) : Node {

    @Suppress("UNUSED_PARAMETER")
    @UseResolver<HouseholdsResolver>(HouseholdsResolver::class)
    fun households(first: Int?): Connection<Household> {
        throw resolverException(this::households)
    }
}

@GraphQLDescription("The members of a household can only be members if they are app users.")
data class Household(@GraphQLID override val id: String, val address: Address) : Node {

    @Suppress("UNUSED_PARAMETER")
    @UseResolver<HouseholdMembersResolver>(HouseholdMembersResolver::class)
    fun members(): Mono<Connection<Member>> {
        throw resolverException(this::members)
    }
}

data class Address(val nameOrNumber: String, val postcode: String)

@GraphQLDescription("Members belong to a household and can list the things they need or offer help for others in the community")
data class Member(@GraphQLID override val id: String, val name: Name, val userId: String, @GraphQLIgnore val householdId: Int) : Node {
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

data class Thing(@GraphQLID override val id: String, val name: String, val quantity: String, val category: Category) : Node

enum class Category {
    Groceries, Medicine, Other
}
