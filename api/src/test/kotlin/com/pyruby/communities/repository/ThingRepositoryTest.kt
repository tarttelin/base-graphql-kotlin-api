package com.pyruby.communities.repository

import com.pyruby.communities.model.Category
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
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

    @Test
    fun `find things for household`() {
        val things = repo.findByHouseholdId(2)
            .collectList()
            .block()
        things?.size `should equal` 2
        things!![0].name `should equal` "Cheese"
    }

    @Test
    fun `saved things are given an id`() {
        val thing = Thing(null, "Ice cream", "1 Litre", Category.Groceries.name, 2)
        val savedThing = repo.save(thing).block()!!
        savedThing.id `should not be` null
        thing.copy(id = savedThing.id) `should equal` savedThing
    }
}
