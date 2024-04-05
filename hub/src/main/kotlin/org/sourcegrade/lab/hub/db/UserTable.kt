package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

internal object UserTable : UUIDTable("sgl_users") {
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex().nullable()
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var username: String by UserTable.username
    var email: String? by UserTable.email

    companion object : UUIDEntityClass<UserEntity>(UserTable)
}
