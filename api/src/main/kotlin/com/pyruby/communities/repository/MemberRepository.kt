package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface MemberRepository : ReactiveCrudRepository<Member, Int> {

    @Query("SELECT * FROM member WHERE householdId = $1")
    fun findByHouseholdId(householdId: Int) : Flux<Member>
}
