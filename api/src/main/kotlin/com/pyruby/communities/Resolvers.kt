package com.pyruby.communities

import com.pyruby.communities.model.*
import com.pyruby.communities.repository.HouseholdRepository
import com.pyruby.communities.repository.MemberRepository
import com.pyruby.communities.repository.ThingRepository
import graphql.relay.Connection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component("householdMembersResolver")
class HouseholdMembersResolver(private val memberRepository: MemberRepository) :  DataFetcher<Mono<Connection<Member>>> {
    override fun get(environment: DataFetchingEnvironment?): Mono<Connection<Member>> {
        val householdId = environment!!.getSource<Household>().id!!
        return memberRepository.findByHouseholdId(householdId.toInt())
            .map { it.toView() }
            .collectList()
            .map { SimpleListConnection(it).get(environment) }
    }
}

private fun com.pyruby.communities.repository.Member.toView(): Member = Member(id?.toString(), Name(preferredName), userId, householdId)

@Component("communityHouseholdsResolver")
class HouseholdsResolver(private val householdRepo: HouseholdRepository) : DataFetcher<Mono<Connection<Household>>> {
    override fun get(environment: DataFetchingEnvironment?) : Mono<Connection<Household>> {
        val communityId = environment!!.getSource<Community>().id?.toInt()!!
        return householdRepo.findByCommunityId(communityId)
            .map { it.toView() }
            .collectList()
            .map {
                SimpleListConnection(it)
                    .get(environment)
            }
    }
}

private fun com.pyruby.communities.repository.Household.toView() = Household(id.toString(), Address(nameOrNumber, postcode))

@Component("memberHouseholdResolver")
class MemberHouseholdResolver(private val householdRepo: HouseholdRepository) : DataFetcher<Mono<Household>> {
    override fun get(environment: DataFetchingEnvironment?): Mono<Household> {
        val householdId = environment?.getSource<Member>()?.householdId!!
        return householdRepo.findById(householdId)
            .map { it.toView()}
    }
}

@Component("memberThingsResolver")
class MemberThingsResolver(private val thingRepo: ThingRepository) : DataFetcher<Mono<Connection<Thing>>> {
    override fun get(environment: DataFetchingEnvironment?) : Mono<Connection<Thing>> {
        val memberId = environment!!.getSource<Member>().id?.toInt()!!
        return thingRepo.findByMemberId(memberId)
            .map { it.toView() }
            .collectList()
            .map {
                SimpleListConnection(it)
                    .get(environment)
            }
    }
}

private fun com.pyruby.communities.repository.Thing.toView() = Thing(name, quantity, Category.valueOf(category))
