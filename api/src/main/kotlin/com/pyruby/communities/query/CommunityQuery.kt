package com.pyruby.communities.query

import com.expediagroup.graphql.annotations.GraphQLContext
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import com.pyruby.communities.context.UserContext
import com.pyruby.communities.model.Community
import com.pyruby.communities.model.Household
import com.pyruby.communities.model.Thing
import com.pyruby.communities.model.Member
import com.pyruby.communities.model.toView
import com.pyruby.communities.repository.CommunityRepository
import com.pyruby.communities.repository.HouseholdRepository
import com.pyruby.communities.repository.MemberRepository
import com.pyruby.communities.repository.ThingRepository
import graphql.relay.Connection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CommunityQuery(
    private val communityRepo: CommunityRepository,
    private val memberRepo: MemberRepository,
    private val householdRepo: HouseholdRepository,
    private val thingRepo: ThingRepository
) : Query {

    @GraphQLDescription("Lookup community")
    fun community(@GraphQLContext context: UserContext): Mono<Community?> {
        return communityRepo.findByUser(context.username)
            .map { it.toView() }
    }

    @GraphQLDescription("Lookup household")
    fun household(@GraphQLContext context: UserContext): Mono<Household> {
        return householdRepo.findByUserId(context.username)
            .map { it.toView() }
    }

    @GraphQLDescription("Lookup member")
    fun member(@GraphQLContext context: UserContext): Mono<Member> {
        return memberRepo.findByUserId(context.username)
            .map { it.toView() }
    }

    @GraphQLDescription("Members things")
    fun things(@GraphQLContext context: UserContext): Mono<List<Thing>>? {
        return thingRepo.findByUserId(context.username)
            .map { it.toView() }
            .collectList()
    }

    @GraphQLDescription("All things for a household")
    fun thingsForHousehold(householdId: String): Mono<Connection<Thing>> {
        return thingRepo.findByHouseholdId(householdId.toInt())
            .map { it.toView() }
            .collectList()
            .map { SimpleListConnection(it).get(newDataFetchingEnvironment().build()) }
    }
}
