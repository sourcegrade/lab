package org.sourcegrade.yougrade.hub.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

object Users : UUIDTable() {
    val username: Column<String> = varchar("username", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 60)
    val email: Column<String> = varchar("email", 100).uniqueIndex()
    // Add other user fields as needed

    fun findByUsername(username: String): User? {
        return transaction {
            select { Users.username eq username }
                .singleOrNull()
                ?.toUser()
        }
    }

    fun findById(id: UUID): User? {
        return transaction {
            select { Users.id eq id }
                .singleOrNull()
                ?.toUser()
        }
    }

    fun createUser(
        username: String,
        password: String,
        email: String,
    ): User {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val userId =
            transaction {
                insertAndGetId {
                    it[Users.username] = username
                    it[Users.password] = hashedPassword
                    it[Users.email] = email
                    // Set other user fields as needed
                }
            }
        return User(userId.value, username, hashedPassword, email)
    }

    fun validateUser(
        username: String,
        password: String,
    ): Boolean {
        val user = findByUsername(username) ?: return false
        return BCrypt.checkpw(password, user.password)
    }
}

data class User(val id: UUID, val username: String, val password: String, val email: String)

data class GraphQLUser(val id: String, val username: String, val email: String)

fun ResultRow.toUser(): User {
    return User(
        this[Users.id].value,
        this[Users.username],
        this[Users.password],
        this[Users.email],
    )
}

fun User.toGraphQLUser(): GraphQLUser {
    return GraphQLUser(
        this.id.toString(),
        this.username,
        this.email,
    )
}

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}
