package com.pyruby.communities.mutation

import com.expediagroup.graphql.annotations.GraphQLContext
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import com.pyruby.communities.context.UserContext
import com.pyruby.communities.model.Category
import com.pyruby.communities.model.Community
import com.pyruby.communities.model.Member
import com.pyruby.communities.model.toView
import com.pyruby.communities.repository.CommunityRepository
import com.pyruby.communities.repository.Household as HouseholdDb
import com.pyruby.communities.repository.HouseholdRepository
import com.pyruby.communities.repository.Member as MemberDb
import com.pyruby.communities.repository.Thing as ThingDb
import com.pyruby.communities.repository.MemberRepository
import com.pyruby.communities.repository.ThingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import com.pyruby.communities.repository.Community as CommunityDb

@Component
class CommunityMutation : Mutation {

    @Autowired lateinit var communityRepo: CommunityRepository
    @Autowired lateinit var householdRepo: HouseholdRepository
    @Autowired lateinit var memberRepo: MemberRepository
    @Autowired lateinit var thingRepo: ThingRepository

    @GraphQLDescription("Creates a community adding the user as a member of a household within that community")
    fun createCommunity(@GraphQLContext context: UserContext, community: CommunityCreation): Mono<Community> =
        communityRepo.save(CommunityDb(null, community.name))
            .flatMap { comm ->
                householdRepo.save(HouseholdDb(null, community.houseNameOrNumber, community.postcode, comm.id!!))
                    .flatMap {
                        memberRepo.save(MemberDb(null, it.id!!, community.preferredName, context.username))
                    }
                    .map { Community(comm.id.toString(), comm.name) }
            }

    @GraphQLDescription("To join an existing community which your household is not currently a part of. A household can only be a member of a single community")
    fun joinCommunity(@GraphQLContext context: UserContext, request: JoinCommunityRequest): Mono<Member> =
        householdRepo.save(HouseholdDb(null, request.houseNameOrNumber, request.postcode, request.communityId.toInt()))
            .flatMap { memberRepo.save(MemberDb(null, it.id!!, request.preferredName, context.username)) }
            .map { it.toView() }

    @GraphQLDescription("To join an existing household in a community. A member can only belong to a single household.")
    fun joinHousehold(@GraphQLContext context: UserContext, request: JoinHouseholdRequest): Mono<Member> =
        memberRepo.save(MemberDb(null, request.householdId.toInt(), request.preferredName, context.username))
            .map { it.toView() }

    fun addThing(@GraphQLContext context: UserContext, request: NewThingRequest): Mono<Member> =
        memberRepo.findByUserId(context.username)
            .flatMap { member ->
                thingRepo.save(ThingDb(null, request.name, request.quantity, request.category.name, member.id!!))
                    .thenReturn(member.toView())
            }

    fun removeThing(@GraphQLContext context: UserContext, request: RemoveThingRequest): Mono<Member> =
        thingRepo.findByUserId(context.username)
            .filter { request.id.toInt() == it.id }
            .next()
            .flatMap { thingRepo.deleteAndReturn(it) }
            .thenReturn(memberRepo.findByUserId(context.username))
            .flatMap { it.map { it.toView() } }

    fun <T, ID> ReactiveCrudRepository<T, ID>.deleteAndReturn(entity: T): Mono<T> =
        delete(entity)
            .thenReturn(entity)
}

data class CommunityCreation(val name: String, val preferredName: String, val houseNameOrNumber: String, val postcode: String)
data class JoinCommunityRequest(val communityId: String, val preferredName: String, val houseNameOrNumber: String, val postcode: String)
data class JoinHouseholdRequest(val householdId: String, val preferredName: String)
data class NewThingRequest(val name: String, val quantity: String, val category: Category)
data class RemoveThingRequest(val id: String)
