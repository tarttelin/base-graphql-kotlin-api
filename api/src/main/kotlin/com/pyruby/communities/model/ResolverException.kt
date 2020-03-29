package com.pyruby.communities.model

import com.pyruby.communities.resolvers.UseResolver
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation

fun resolverException(field: KCallable<*>) =
    ResolverException("Field: ${field.name} should be handled by ${field.findAnnotation<UseResolver<*>>()?.resolver?.simpleName ?: "Undefined"}")

class ResolverException(msg: String) : RuntimeException(msg)
