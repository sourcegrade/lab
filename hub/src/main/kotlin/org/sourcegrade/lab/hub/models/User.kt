package org.sourcegrade.lab.hub.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object Users : Models("users") {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
}

@Serializable
class UserDTO(
    val id: String,
    val username: String,
    val email: String,
) {
    suspend fun courses(): List<CourseDTO> {
        return newSuspendedTransaction {
            User.findById(this@UserDTO.id)?.courses?.map { it.toDTO() }
                ?: throw IllegalArgumentException("No User with id $id found")
        }
    }
}

class User(id: EntityID<String>) : Model<UserDTO>(id) {
    companion object : EntityClass<String, User>(Users)

    var username by Users.username

    //    var password by Users.password
    var email by Users.email
    var courses by Course via CourseMembers

    override fun toDTO(): UserDTO {
        return UserDTO(
            this.id.value,
            this.username,
            this.email,
        )
    }
}

fun ResultRow.toUserDTO(): UserDTO {
    return UserDTO(
        this[Users.id].value,
        this[Users.username],
        this[Users.email],
    )
}
