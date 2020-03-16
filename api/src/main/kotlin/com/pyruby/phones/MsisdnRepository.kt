package com.pyruby.phones

import com.pyruby.phones.model.Msisdn
import org.springframework.stereotype.Component

@Component("msisdnRepo")
class MsisdnRepository {

    fun findByCustomerId(customerId: String, isPrimary: Boolean): List<Msisdn> {
        val primary = listOf(Msisdn("12345", "Ages"))
        return if (isPrimary) primary else primary.plus(Msisdn("123456", "More recent"))
    }
}
