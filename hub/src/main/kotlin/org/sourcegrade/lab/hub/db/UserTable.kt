package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.sourcegrade.lab.hub.domain.User
import java.util.UUID

internal object UserTable : UUIDTable("sgl_users") {
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var username: String by UserTable.username
    var email: String by UserTable.email

    companion object : UUIDEntityClass<UserEntity>(UserTable)
}

internal fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    username = this[UserTable.username],
    email = this[UserTable.email],
)
