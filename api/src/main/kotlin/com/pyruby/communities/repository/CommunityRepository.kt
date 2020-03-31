package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CommunityRepository : ReactiveCrudRepository<Community, Int> {
    @Query("""
        SELECT *
        FROM community c
        WHERE c.id in (
            SELECT COMMUNITY_ID
            FROM household h
            WHERE h.COMMUNITY_ID = c.id
            AND h.id in (
                SELECT HOUSEHOLD_ID
                FROM member m
                WHERE m.USER_ID = :userId
            )
        )
    """)
    fun findByUser(userId: String): Mono<Community>
}
