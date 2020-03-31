package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MemberRepository : ReactiveCrudRepository<Member, Int> {

    @Query("SELECT * FROM member WHERE HOUSEHOLD_ID = :householdId")
    fun findByHouseholdId(householdId: Int): Flux<Member>

    @Query("SELECT * FROM member WHERE USER_ID = :userId")
    fun findByUserId(userId: String): Mono<Member>
}
