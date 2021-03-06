package com.pyruby.communities.logging

import com.pyruby.communities.context.UserContext
import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Component
class LoggingInstrumentation : SimpleInstrumentation() {

    private val logger by LoggerDelegate()

    override fun createState(parameters: InstrumentationCreateStateParameters?): InstrumentationState {
        if (parameters != null) {
            val phoneContext = parameters.executionInput.context as UserContext
            val query = parameters.executionInput.query
            return Journal(query = query, msisdn = phoneContext.username, startTime = LocalDateTime.now())
        }
        return Journal()
    }

    override fun instrumentExecutionResult(executionResult: ExecutionResult?, parameters: InstrumentationExecutionParameters?): CompletableFuture<ExecutionResult> {
        val instrumentationState = parameters?.getInstrumentationState<Journal>()
        logger.debug("journal", instrumentationState)
        return super.instrumentExecutionResult(executionResult, parameters)
    }
}

data class Journal(val query: String? = null, val msisdn: String? = null, val startTime: LocalDateTime? = null, val endTime: LocalDateTime? = null) : InstrumentationState
