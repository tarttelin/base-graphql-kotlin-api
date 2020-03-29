package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import kotlin.test.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommunityRepositoryTest {

    @Autowired lateinit var communityRepository: CommunityRepository

    @Test
    fun `load community by id`() {
        val community = communityRepository.findById(1)
            .block()
        community?.name `should equal` "Deans Farm"
    }

    @Test
    fun `load community by username of a member of that community`() {
        val community = communityRepository.findByUser("chris_t")
            .block()
        community?.name `should equal` "Deans Farm"
    }
}
