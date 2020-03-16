package com.pyruby.phones.model

import com.pyruby.phones.resolvers.UseResolver
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation

fun resolverException(field: KCallable<*>) =
    ResolverException("Field: ${field.name} should be handled by ${field.findAnnotation<UseResolver<*>>()?.resolver?.simpleName ?: "Undefined"}")

class ResolverException(msg: String) : RuntimeException(msg)
