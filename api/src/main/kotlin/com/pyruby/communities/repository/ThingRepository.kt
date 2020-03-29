package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ThingRepository : ReactiveCrudRepository<Thing, Int> {

    @Query("SELECT * FROM thing WHERE MEMBER_ID = :memberId")
    fun findByMemberId(memberId: Int) : Flux<Thing>

}
