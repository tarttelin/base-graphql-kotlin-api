package com.pyruby.communities.context

import com.expediagroup.graphql.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.pyruby.communities.resolvers.UseResolver
import graphql.schema.DataFetcher
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetcherFactoryEnvironment
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

/**
 * Framework class that looks at the fields in a query and looks for the <code>UseResolver</code> annotation, then
 * looks up the named resolver from Spring BeanFactory.
 */
@Component
class CustomDataFetcherFactoryProvider(objectMapper: ObjectMapper) : SimpleKotlinDataFetcherFactoryProvider(objectMapper), BeanFactoryAware {

    private lateinit var beanFactory: BeanFactory
    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>): DataFetcherFactory<Any?> =
        resolverOrDefault(kFunction) { super.functionDataFetcherFactory(target, kFunction) }

    override fun propertyDataFetcherFactory(kClass: KClass<*>, kProperty: KProperty<*>): DataFetcherFactory<Any?> =
        resolverOrDefault(kProperty) { super.propertyDataFetcherFactory(kClass, kProperty) }

    @Suppress("UNCHECKED_CAST")
    private fun resolverOrDefault(annotatedElem: KAnnotatedElement, default: () -> DataFetcherFactory<Any?>) =
        MonoWrapper(
            if (annotatedElem.findAnnotation<UseResolver<*>>() != null) {
                val resolverClass = annotatedElem.findAnnotation<UseResolver<*>>() as UseResolver
                val resolver = beanFactory.getBean(resolverClass.resolver.java) as DataFetcher<Any?>
                DataFetcherFactory { resolver }
            } else {
                default()
            }
        )
}

class MonoWrapper(private val delegate: DataFetcherFactory<Any?>) : DataFetcherFactory<Any?> {

    override fun get(environment: DataFetcherFactoryEnvironment?) = DataFetcher<Any?> {
        when (val result = delegate.get(environment).get(it)) {
            is Mono<*> -> result.toFuture()
            else -> result
        }
    }
}
