package com.pyruby.phones.resolvers

import graphql.schema.DataFetcher
import kotlin.reflect.KClass

annotation class UseResolver<T : DataFetcher<*>>(val resolver: KClass<T>)
