package com.pyruby.communities.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CommunityRepository: ReactiveCrudRepository<Community, Int> {
    @Query("""
        SELECT *
        FROM community c
        WHERE c.id in (
            SELECT communityId
            FROM household h
            WHERE h.communityId = c.id
            AND id in (
                SELECT communityId
                FROM member m
                WHERE m.userId = :userId
            )
        )
    """)
    fun findByUser(userId: String): Mono<Community>

}
