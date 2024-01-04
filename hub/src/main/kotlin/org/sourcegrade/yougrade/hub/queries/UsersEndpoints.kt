package org.sourcegrade.yougrade.hub.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.sourcegrade.yougrade.hub.models.User
import org.sourcegrade.yougrade.hub.models.UserDTO
import org.sourcegrade.yougrade.hub.models.Users
import org.sourcegrade.yougrade.hub.models.toUserDTO

class UsersEndpoints : Query {
    @GraphQLDescription("Get a list of all users")
    suspend fun getAllUsers(): List<UserDTO> {
        return User.all().map { it.toUserDTO() }
    }

    suspend fun getUserById(id: String): UserDTO? {
        return newSuspendedTransaction {
            User.findById(id)?.toUserDTO()
        }
    }

    suspend fun setUserEmail(
        id: String,
        @GraphQLName("email") newEmail: String,
    ): UserDTO {
        return newSuspendedTransaction {
            Users.update({ Users.id eq id }) {
                it[Users.email] = newEmail
            }
            Users.findById(id)!!.toUserDTO()
        }
    }
}
