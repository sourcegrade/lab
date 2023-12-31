package org.sourcegrade.yougrade.hub.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.sourcegrade.yougrade.hub.models.*

class UsersEndpoints : Query {
    @GraphQLDescription("Get a list of all users")
    fun getAllUsers(): List<GraphQLUser> {
        return transaction {
            Users.selectAll().map { row -> row.toUser().toGraphQLUser() }
        }
    }

    fun getUserById(id: String): GraphQLUser? {
        return transaction {
            Users.findById(id.toUUID())?.toGraphQLUser()
        }
    }

    fun setUserEmail(
        id: String,
        @GraphQLName("email") newEmail: String,
    ): GraphQLUser {
        return transaction {
            Users.update({ Users.id eq id.toUUID() }) {
                it[Users.email] = newEmail
            }
            getUserById(id) ?: throw Exception("User not found")
        }
    }
}
