package com.pyruby.communities.resolvers

import graphql.schema.DataFetcher
import kotlin.reflect.KClass

annotation class UseResolver<T : DataFetcher<*>>(val resolver: KClass<T>)
