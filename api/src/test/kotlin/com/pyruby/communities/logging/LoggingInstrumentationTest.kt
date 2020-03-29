package com.pyruby.communities.logging

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.core.OutputStreamAppender
import com.pyruby.communities.context.UserContext
import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.schema.GraphQLSchema
import io.mockk.every
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import io.mockk.mockk
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.time.LocalDateTime
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LoggingInstrumentationTest {
    @Test
    fun `creating state with null instrumentation parameters returns a blank journal`() {
        val instrumentation = LoggingInstrumentation()
        val state = instrumentation.createState(null)
        state `should equal` Journal()
    }

    @Test
    fun `creating state with valid instrumentation parameters returns a journal with details`() {
        val instrumentation = LoggingInstrumentation()
        val executionInput = ExecutionInput.newExecutionInput("someQuery")
            .context(UserContext("someMsisdn"))
            .build()
        val graphQLSchema: GraphQLSchema = mockk<GraphQLSchema>()
        val parameters = InstrumentationCreateStateParameters(graphQLSchema, executionInput)
        val state: Journal = instrumentation.createState(parameters) as Journal
        state.msisdn `should equal` "someMsisdn"
        state.query `should equal` "someQuery"
        state.startTime `should not be` null
        state.endTime `should be` null
    }

    @Test
    fun `instrumentExecutionResult should log nothing given no parameters`() {
        val byteArrayOutputStream = initLogger()
        val instrumentation = LoggingInstrumentation()
        instrumentation.instrumentExecutionResult(null, null)

        byteArrayOutputStream.toString(Charset.defaultCharset()).length `should be` 0
    }

    @Test
    fun `instrumentExecutionResult should log journal given parameters contain journal`() {
        val byteArrayOutputStream = initLogger()
        val execParams = mockk<InstrumentationExecutionParameters>()
        val execResults = mockk<ExecutionResult>()

        val expected = Journal(query = "aQuery", msisdn = "123098", startTime = LocalDateTime.now(), endTime = LocalDateTime.now().plusMinutes(1))
        every<Journal> { execParams.getInstrumentationState() } returns expected

        val instrumentation = LoggingInstrumentation()
        val result = instrumentation.instrumentExecutionResult(execResults, execParams)

        byteArrayOutputStream.toString(Charset.defaultCharset()) `should contain` "123098"
        result.join() `should be` execResults
    }

    private fun initLogger(): ByteArrayOutputStream {
        val logger = LoggerFactory.getLogger(LoggingInstrumentation::class.java) as Logger
        logger.loggerContext.reset()
        val appender = OutputStreamAppender<ILoggingEvent>()
        appender.context = LoggerContext()
        val journalLayout = JournalLayout()
        journalLayout.jsonFormatter = JacksonJsonFormatter()
        appender.setLayout(journalLayout)
        val byteArrayOutputStream = ByteArrayOutputStream()
        appender.outputStream = byteArrayOutputStream
        appender.start()
        logger.addAppender(appender)
        return byteArrayOutputStream
    }
}
