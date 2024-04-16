package org.sourcegrade.lab.hub.domain

import kotlin.reflect.KClass

interface UserRepository : MutableRepository<User, User.CreateDto> {
    override val entityType: KClass<User>
        get() = User::class
}
