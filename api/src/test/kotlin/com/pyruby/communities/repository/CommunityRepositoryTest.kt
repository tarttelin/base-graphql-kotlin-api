package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import kotlin.test.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommunityRepositoryTest {

    @Autowired lateinit var repo: CommunityRepository

    @Test
    fun `load community by id`() {
        val community = repo.findById(1)
            .block()
        community?.name `should equal` "Deans Farm"
    }

    @Test
    fun `load community by username of a member of that community`() {
        val community = repo.findByUser("kelz")
            .block()
        community?.name `should equal` "Amersham massiv"
    }

    @Test
    fun `load community by username of a member that has no community`() {
        val community = repo.findByUser("unknown_user")
            .block()
        community?.name `should equal` null
    }

    @Test
    fun `saved communities are given an id`() {
        val community = Community(null, "Some place nice")
        val savedCommunity = repo.save(community).block()!!
        savedCommunity.id `should not be` null
        community.copy(id = savedCommunity.id) `should equal` savedCommunity
    }
}
