package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface HouseholdRepository : ReactiveCrudRepository<Household, Int> {

    @Query("SELECT * FROM household WHERE communityId = $1")
    fun findByCommunityId(communityId: Int) : Flux<Household>
}
