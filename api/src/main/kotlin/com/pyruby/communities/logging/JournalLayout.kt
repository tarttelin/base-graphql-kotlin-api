package com.pyruby.communities.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.json.JsonLayoutBase

class JournalLayout : JsonLayoutBase<ILoggingEvent>() {
    override fun toJsonMap(e: ILoggingEvent?): MutableMap<Any?, Any?> {
        val journal = e!!.argumentArray[0] as Journal
        return mutableMapOf("msisdn" to journal.msisdn)
    }
}
