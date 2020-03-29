package com.pyruby.communities.query

import com.expediagroup.graphql.annotations.GraphQLContext
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import com.pyruby.communities.repository.CommunityRepository
import com.pyruby.communities.context.UserContext
import com.pyruby.communities.model.Community
import com.pyruby.communities.model.Household
import com.pyruby.communities.model.Member
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class CommunityQuery(private val communityRepo: CommunityRepository) : Query {

    @GraphQLDescription("Lookup community")
    fun community(@GraphQLContext context: UserContext): Mono<Community> {
        return communityRepo.findByUser(context.username!!)
            .map { toModel(it) }
    }

    private fun toModel(community: com.pyruby.communities.repository.Community?) =
        if (community == null) {
            println("*******  Community is null")
            null
        } else {
            println("*******  Community is ${community.name}")
            Community(community.id.toString(), community.name)
        }


    @GraphQLDescription("Lookup household")
    fun household(@GraphQLContext context: UserContext): Household? {
        return null
    }

    @GraphQLDescription("Lookup household")
    fun member(@GraphQLContext context: UserContext): Member? {
        return null
    }
}
