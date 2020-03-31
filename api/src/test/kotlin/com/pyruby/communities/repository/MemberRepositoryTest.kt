package com.pyruby.communities.repository

import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
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

    @Test
    fun `saved members are given an id`() {
        val member = Member(null, 1, "Uncle Tom Cobbly", "ut_cobbly")
        val savedMember = repo.save(member).block()!!
        savedMember.id `should not be` null
        member.copy(id = savedMember.id) `should equal` savedMember
    }
}
