package com.pyruby.communities.model

import com.pyruby.communities.repository.Thing as ThingDb
import com.pyruby.communities.repository.Member as MemberDb
import com.pyruby.communities.repository.Household as HouseholdDb
import com.pyruby.communities.repository.Community as CommunityDb

fun CommunityDb.toView() =
    Community(id.toString(), name)

fun MemberDb.toView() =
    Member(id.toString(), Name(preferredName), userId, householdId)

fun HouseholdDb.toView() =
    Household(id.toString(), Address(nameOrNumber, postcode))

fun ThingDb.toView() =
    Thing(name, quantity, Category.valueOf(category))
