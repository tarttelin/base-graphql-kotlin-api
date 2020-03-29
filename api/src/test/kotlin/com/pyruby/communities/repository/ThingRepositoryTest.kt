package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class ThingRepositoryTest {

    @Autowired lateinit var repo: ThingRepository

    @Test
    fun `find things for member`() {
        val things = repo.findByMemberId(1)
            .collectList()
            .block()
        things?.size `should equal` 1
        things!![0].name `should equal` "Strong plain bread flour"
    }
}
