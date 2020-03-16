package com.pyruby.phones

import com.pyruby.phones.model.Customer
import com.pyruby.phones.model.CustomerName
import org.springframework.stereotype.Component

@Component("customerRepo")
class CustomerRepository {
    fun get(msisdn: String): Customer? {
        return Customer(msisdn, CustomerName("Bob", "Smith"))
    }
}
