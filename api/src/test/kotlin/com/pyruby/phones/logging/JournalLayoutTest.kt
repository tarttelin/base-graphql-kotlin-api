package com.pyruby.phones.logging

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.core.OutputStreamAppender
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import org.amshove.kluent.`should be equal to`

class JournalLayoutTest {
    @Test
    fun `Logger writes journal`() {
        val logger = LoggerFactory.getLogger("journal") as Logger

        logger.loggerContext.reset()
        val appender = OutputStreamAppender<ILoggingEvent>()
        val journalLayout = JournalLayout()
        journalLayout.jsonFormatter = JacksonJsonFormatter()
        appender.setLayout(journalLayout)
        appender.context = logger.loggerContext
        val byteArrayOutputStream = ByteArrayOutputStream()
        appender.outputStream = byteArrayOutputStream

        appender.start()

        logger.addAppender(appender)
        logger.debug("Journal", Journal(msisdn = "msisdn123"))
        val msg = byteArrayOutputStream.toString(Charset.defaultCharset())
        val journal = ObjectMapper().readValue(msg, Journal::class.java)
        journal.msisdn!! `should be equal to` "msisdn123"
    }
}
