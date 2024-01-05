package org.sourcegrade.yougrade.hub.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.yougrade.hub.models.User
import org.sourcegrade.yougrade.hub.models.UserDTO

class UserQueries : Query {
    @GraphQLDescription("Get a list of all users")
    suspend fun getAllUsers(): List<UserDTO> {
        return newSuspendedTransaction {
            User.all().map { it.toDTO() }
        }
    }

    suspend fun user(id: String): UserDTO {
        return newSuspendedTransaction {
            User.findById(id)?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }
}

class UserMutation {
    private fun requireId(environment: DataFetchingEnvironment): String {
        return environment.executionStepInfo.parent?.arguments?.get("id") as? String
            ?: throw IllegalArgumentException("id is required")
    }

    suspend fun create(
        environment: DataFetchingEnvironment,
        input: UserDTO,
    ): UserDTO {
        return newSuspendedTransaction {
            User.new {
                this.email = input.email
                this.username = input.username
            }.toDTO()
        }
    }

    suspend fun delete(environment: DataFetchingEnvironment): UserDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.apply {
                delete()
            }?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    suspend fun fetch(environment: DataFetchingEnvironment): UserDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    suspend fun updateEmail(
        environment: DataFetchingEnvironment,
        @GraphQLName("email") newEmail: String,
    ): String {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.apply {
                email = newEmail
            }?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }.email
    }

    suspend fun updateUsername(
        environment: DataFetchingEnvironment,
        @GraphQLName("username") newUsername: String,
    ): String {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.apply {
                username = newUsername
            }?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }.username
    }
}

class UserMutations : Mutation {
    suspend fun userMutation(
        environment: DataFetchingEnvironment,
        id: String? = null,
    ): UserMutation {
        return UserMutation()
    }
}
