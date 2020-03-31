package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
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

    @Test
    fun `load household by user id`() {
        val household = repo.findByUserId("shipsmouse")
            .block()
        household?.postcode `should equal` "RG4 5JZ"
    }

    @Test
    fun `populates the id on insert`() {
        val household = Household(null, "12", "NW1 8BP", 1)
        val savedHousehold = repo.save(household)
            .block()!!
        savedHousehold.id `should not be` null
        savedHousehold.nameOrNumber `should equal` household.nameOrNumber
        savedHousehold.postcode `should equal` household.postcode
        savedHousehold.communityId `should equal` household.communityId
    }
}
