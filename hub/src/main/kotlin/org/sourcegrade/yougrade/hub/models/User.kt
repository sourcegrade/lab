package org.sourcegrade.yougrade.hub.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow

object Users : Models("users") {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
//    val password = varchar("password", 255).nullable()
    // Add other user fields as needed

    suspend fun findByUsername(username: String): User? {
        return User.find { Users.username eq username }
            .singleOrNull()
    }

//
    suspend fun findById(id: String): User? {
        return User.findById(id)
    }
//
//    fun createUser(
//        username: String,
//        password: String,
//        email: String,
//    ): User {
//        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
//        val userId =
//            transaction {
//                insertAndGetId {
//                    it[Users.username] = username
//                    it[Users.password] = hashedPassword
//                    it[Users.email] = email
//                    // Set other user fields as needed
//                }
//            }
//        return User(userId.value, username, hashedPassword, email)
//    }
//
//    fun validateUser(
//        username: String,
//        password: String,
//    ): Boolean {
//        val user = findByUsername(username) ?: return false
//        return BCrypt.checkpw(password, user.password)
//    }
}

class User(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, User>(Users)

    var username by Users.username

//    var password by Users.password
    var email by Users.email
}

@Serializable
class UserDTO(
    val id: String,
    val username: String,
    val email: String,
)

fun User.toUserDTO(): UserDTO {
    return UserDTO(
        this.id.value,
        this.username,
        this.email,
    )
}

fun ResultRow.toUserDTO(): UserDTO {
    return UserDTO(
        this[Users.id].value,
        this[Users.username],
        this[Users.email],
    )
}
