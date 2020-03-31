package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ThingRepository : ReactiveCrudRepository<Thing, Int> {

    @Query("SELECT * FROM thing WHERE MEMBER_ID = :memberId")
    fun findByMemberId(memberId: Int): Flux<Thing>

    @Query("SELECT * FROM thing WHERE member_id IN (SELECT id FROM member WHERE user_id = :userId)")
    fun findByUserId(userId: String): Flux<Thing>

    @Query("SELECT * FROM thing WHERE member_id IN (SELECT id FROM member WHERE household_id = :householdId)")
    fun findByHouseholdId(householdId: Int): Flux<Thing>
}
