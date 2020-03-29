package com.pyruby.communities

import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.BeanCreationException

internal class ApplicationKtTest {
    @Test
    fun `Call main with invalid config fails`() {
        invoking {
            main(listOf("--spring.config.name=rupert_the_missing").toTypedArray())
        } `should throw` BeanCreationException::class
    }
}
