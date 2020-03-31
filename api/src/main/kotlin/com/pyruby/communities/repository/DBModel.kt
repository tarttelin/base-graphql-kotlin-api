package com.pyruby.communities.repository

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("community")
data class Community(@Id val id: Int? = null, val name: String)

@Table("household")
data class Household(
    @Id val id: Int? = null,
    val nameOrNumber: String,
    val postcode: String,
    val communityId: Int
)

@Table("member")
data class Member(@Id val id: Int? = null, val householdId: Int, val preferredName: String, val userId: String)

@Table("thing")
data class Thing(
    @Id val id: Int? = null,
    val name: String,
    val quantity: String,
    val category: String,
    val memberId: Int
)
