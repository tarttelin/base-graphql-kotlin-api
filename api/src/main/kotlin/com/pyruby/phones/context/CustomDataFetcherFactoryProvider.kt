package com.pyruby.phones.context

import com.expediagroup.graphql.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.pyruby.phones.resolvers.UseResolver
import graphql.schema.DataFetcher
import graphql.schema.DataFetcherFactory
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.getBean
import org.springframework.stereotype.Component
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

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

    private fun resolverOrDefault(annotatedElem: KAnnotatedElement, default: () -> DataFetcherFactory<Any?>) =
        if (annotatedElem.findAnnotation<UseResolver<*>>() != null) {
            val resolverClass = annotatedElem.findAnnotation<UseResolver<*>>() as UseResolver
            val resolver = beanFactory.getBean(resolverClass.resolver) as DataFetcher<Any?>
            DataFetcherFactory { resolver }
        } else {
            default()
        }
}
