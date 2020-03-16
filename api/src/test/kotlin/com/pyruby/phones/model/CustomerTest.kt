package com.pyruby.phones.model

import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

internal class CustomerTest {
    @Test
    fun `Customer msisdn function throws an exception when called directly`() {
        invoking {
            Customer("123", CustomerName("Bob", "Crowley")).msisdns(primary = false)
        } `should throw` ResolverException::class
    }
}
