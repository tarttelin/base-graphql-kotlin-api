package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface HouseholdRepository : ReactiveCrudRepository<Household, Int> {

    @Query("SELECT * FROM household WHERE COMMUNITY_ID = :communityId")
    fun findByCommunityId(communityId: Int): Flux<Household>

    @Query("SELECT * FROM household WHERE id IN (SELECT household_id FROM member where user_id = :userId)")
    fun findByUserId(userId: String): Mono<Household>
}
