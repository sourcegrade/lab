package org.sourcegrade.yougrade.hub.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow

object Users : Models("users") {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
}

class User(id: EntityID<String>) : Model<UserDTO>(id) {
    companion object : EntityClass<String, User>(Users)

    var username by Users.username

//    var password by Users.password
    var email by Users.email

    override fun toDTO(): UserDTO {
        return UserDTO(
            this.id.value,
            this.username,
            this.email,
        )
    }
}

@Serializable
class UserDTO(
    val id: String,
    val username: String,
    val email: String,
)

fun ResultRow.toUserDTO(): UserDTO {
    return UserDTO(
        this[Users.id].value,
        this[Users.username],
        this[Users.email],
    )
}
