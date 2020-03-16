package com.pyruby.phones.model

import org.amshove.kluent.`should end with`
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ResolverExceptionKtTest {

    @Test
    fun `Creating a resolver exception with a non annotated function returns a different message`() {
        val exception = resolverException(Customer::name)
        exception.message!! `should end with` "Undefined"
    }
}
