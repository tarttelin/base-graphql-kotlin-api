package com.pyruby.communities.model

import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ModelTest {
    @Test
    fun `community households function throws resolver exception`() {
        val model = Community("id1", "Some place nice")
        invoking { model.households(first = 10) } `should throw`
            ResolverException::class withMessage "Field: households should be handled by HouseholdsResolver"
    }

    @Test
    fun `household members function throws resolver exception`() {
        val model = Household("id3", Address("999", "Letsbe Avenue"))
        invoking { model.members() } `should throw`
            ResolverException::class withMessage "Field: members should be handled by HouseholdMembersResolver"
    }

    @Test
    fun `member things function throws resolver exception`() {
        val model = Member("123", Name("Bob Hoskins"), "bobby_h", 123)
        invoking { model.things() } `should throw`
            ResolverException::class withMessage "Field: things should be handled by MemberThingsResolver"
    }

    @Test
    fun `member household function throws resolver exception`() {
        val model = Member("123", Name("Bob Hoskins"), "bobby_h", 123)
        invoking { model.household() } `should throw`
            ResolverException::class withMessage "Field: household should be handled by MemberHouseholdResolver"
    }
}
