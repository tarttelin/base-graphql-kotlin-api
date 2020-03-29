package com.pyruby.communities.resolvers

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import graphql.Scalars
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.*
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

class RelaySchemaHook(override val wiringFactory: KotlinDirectiveWiringFactory) : SchemaGeneratorHooks {

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        if (type.classifier == Connection::class) {
            val typeName = type.javaType.typeName
            val suffix = typeName.substring(typeName.lastIndexOf(".") + 1).removeSuffix(">")
            return connectionType(suffix)
        }
        return null
    }

    override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
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
