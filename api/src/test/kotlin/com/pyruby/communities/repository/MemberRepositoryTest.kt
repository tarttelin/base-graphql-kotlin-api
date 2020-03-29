package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    lateinit var repo: MemberRepository

    @Test
    fun `load members by household id`() {
        val member = repo.findByHouseholdId(1)
            .blockFirst()
        member?.preferredName `should equal` "Chris"
    }

}
