package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class HouseholdRepositoryTest {
    @Autowired
    lateinit var repo: HouseholdRepository

    @Test
    fun `load household by community id`() {
        val household = repo.findByCommunityId(1)
            .blockFirst()
        household?.postcode `should equal` "RG4 5JZ"
    }
}
