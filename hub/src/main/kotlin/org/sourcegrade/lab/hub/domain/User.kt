package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.Users
import java.util.UUID

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    var username: String by Users.username
    var email: String by Users.email

    companion object : EntityClass<UUID, User>(Users)
}
