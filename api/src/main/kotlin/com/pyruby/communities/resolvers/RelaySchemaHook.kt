package com.pyruby.communities.resolvers

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.Scalars
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLTypeReference
import graphql.schema.GraphQLSchema.Builder
import graphql.schema.GraphQLType
import reactor.core.publisher.Mono
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

class RelaySchemaHook(override val wiringFactory: KotlinDirectiveWiringFactory) : SchemaGeneratorHooks {

    private val generatedTypes = mutableMapOf<String, GraphQLType>()

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        if (type.classifier == Connection::class) {
            println("  Create connection type for ${type.javaType.typeName}")
            val typeName = type.javaType.typeName
            return if (generatedTypes.contains(typeName)) {
                generatedTypes[typeName]
            } else {
                val suffix = typeName.substring(typeName.lastIndexOf(".") + 1).removeSuffix(">")
                val gqlType = connectionType(suffix)
                generatedTypes[typeName] = gqlType
                gqlType
            }
        }
        return null
    }

    override fun willResolveMonad(type: KType): KType = when (type.classifier) {
        Mono::class -> type.arguments.first().type!!
        else -> type
    }

    override fun willBuildSchema(builder: Builder): Builder {
        return builder.additionalType(Relay.pageInfoType)
    }
}

internal fun connectionType(prefix: String) =
    GraphQLObjectType.newObject()
        .name(prefix + "Connection")
        .description("A connection to a list of items.")
        .field(newFieldDefinition()
            .name("edges")
            .description("a list of edges")
            .type(GraphQLList.list(edgeType(prefix))))
        .field(newFieldDefinition()
            .name("pageInfo")
            .description("details about this specific page")
            .type(GraphQLNonNull.nonNull(GraphQLTypeReference.typeRef("PageInfo"))))
        .build()

internal fun edgeType(prefix: String) =
    GraphQLObjectType.newObject()
        .name(prefix + "Edge")
        .description("An edge in a connection")
        .field(newFieldDefinition()
            .name("node")
            .type(GraphQLNonNull.nonNull(GraphQLTypeReference.typeRef(prefix)))
            .description("The item at the end of the edge"))
        .field(newFieldDefinition()
            .name("cursor")
            .type(GraphQLNonNull.nonNull(Scalars.GraphQLString))
            .description("cursor marks a unique position or index into the connection"))
        .build()
