package com.pyruby.communities.mutation

import com.expediagroup.graphql.annotations.GraphQLContext
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import com.pyruby.communities.context.UserContext
import com.pyruby.communities.model.Community
import com.pyruby.communities.repository.CommunityRepository
import com.pyruby.communities.repository.Household
import com.pyruby.communities.repository.HouseholdRepository
import com.pyruby.communities.repository.Member
import com.pyruby.communities.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import com.pyruby.communities.repository.Community as CommunityDb

@Component
class CommunityMutation : Mutation {

    @Autowired lateinit var communityRepo: CommunityRepository
    @Autowired lateinit var householdRepo: HouseholdRepository
    @Autowired lateinit var memberRepo: MemberRepository

    @GraphQLDescription("Creates a community adding the user as a member of a household within that community")
    fun createCommunity(@GraphQLContext context: UserContext, community: CommunityCreation): Mono<Community> {
        val result = communityRepo.save(CommunityDb(null, community.name))
            .flatMap { comm ->
                householdRepo.save(Household(null, community.houseNameOrNumber, community.postcode, comm.id!!))
                    .flatMap {
                        memberRepo.save(Member(null, it.id!!, community.preferredName, context.username))
                    }
                    .map { Community(comm.id.toString(), comm.name) }
            }
        return result
    }
}

data class CommunityCreation(val name: String, val preferredName: String, val houseNameOrNumber: String, val postcode: String)
