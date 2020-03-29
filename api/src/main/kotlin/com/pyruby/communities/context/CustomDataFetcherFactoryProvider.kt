package com.pyruby.communities.context

import com.expediagroup.graphql.execution.FunctionDataFetcher
import com.expediagroup.graphql.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.pyruby.communities.resolvers.UseResolver
import graphql.schema.DataFetcher
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetcherFactoryEnvironment
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.getBean
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
class CustomDataFetcherFactoryProvider(private val objectMapper: ObjectMapper) : SimpleKotlinDataFetcherFactoryProvider(objectMapper), BeanFactoryAware {

    private lateinit var beanFactory: BeanFactory
    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>): DataFetcherFactory<Any?> =
        resolverOrDefault(kFunction) { DataFetcherFactory { MonoWrapper(target, kFunction, objectMapper) } }

    override fun propertyDataFetcherFactory(kClass: KClass<*>, kProperty: KProperty<*>): DataFetcherFactory<Any?> =
        resolverOrDefault(kProperty) { super.propertyDataFetcherFactory(kClass, kProperty) }

    private fun resolverOrDefault(annotatedElem: KAnnotatedElement, default: () -> DataFetcherFactory<Any?>) =
        if (annotatedElem.findAnnotation<UseResolver<*>>() != null) {
            val resolverClass = annotatedElem.findAnnotation<UseResolver<*>>() as UseResolver
            val resolver = beanFactory.getBean(resolverClass.resolver.java) as DataFetcher<Any?>
            DataFetcherFactory { resolver }
        } else {
            default()
        }
}

class MonoWrapper(target: Any?, fn: KFunction<*>, objectMapper: ObjectMapper) : FunctionDataFetcher(target, fn, objectMapper) {

    override fun get(environment: DataFetchingEnvironment): Any? = when (val result = super.get(environment)) {
        is Mono<*> -> result.toFuture()
        else -> result
    }

}
