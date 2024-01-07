package org.sourcegrade.yougrade.hub.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.yougrade.hub.models.CourseDTO
import org.sourcegrade.yougrade.hub.models.User
import org.sourcegrade.yougrade.hub.models.UserDTO

@GraphQLDescription("Query collection for users")
class UserQuery {
    private fun requireId(environment: DataFetchingEnvironment): String {
        return environment.executionStepInfo.parent?.arguments?.get("id") as? String
            ?: throw IllegalArgumentException("id is required")
    }

    @GraphQLDescription("Get a list of all users")
    suspend fun fetchAll(): List<UserDTO> {
        return newSuspendedTransaction {
            User.all().map { it.toDTO() }
        }
    }

    @GraphQLDescription("Get a single user by id")
    suspend fun fetch(environment: DataFetchingEnvironment): UserDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    @GraphQLDescription("Get the courses of a user by id")
    suspend fun courses(environment: DataFetchingEnvironment): List<CourseDTO> {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.courses?.toList()?.map { it.toDTO() } ?: throw IllegalArgumentException("No user with id $id found")
        }
    }
}

class UserQueries : Query {
    suspend fun user(
        environment: DataFetchingEnvironment,
        id: String? = null,
    ): UserQuery {
        return UserQuery()
    }
}

@GraphQLDescription("Mutation collection for users")
class UserMutation {
    private fun requireId(environment: DataFetchingEnvironment): String {
        return environment.executionStepInfo.parent?.arguments?.get("id") as? String
            ?: throw IllegalArgumentException("id is required")
    }

    data class CreateUserInput(val email: String, val username: String, val password: String? = null)

    @GraphQLDescription("Create a new user")
    suspend fun create(
        environment: DataFetchingEnvironment,
        input: CreateUserInput,
    ): UserDTO {
        return newSuspendedTransaction {
            User.new {
                this.email = input.email
                this.username = input.username
            }.toDTO()
        }
    }

    @GraphQLDescription("Delete a user by id")
    suspend fun delete(environment: DataFetchingEnvironment): UserDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.apply {
                delete()
            }?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    @GraphQLDescription("Get a single user by id")
    suspend fun fetch(environment: DataFetchingEnvironment): UserDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            User.findById(id)?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    @GraphQLDescription("Update a user's email")
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

    @GraphQLDescription("Update a user's username")
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
    suspend fun user(
        environment: DataFetchingEnvironment,
        id: String? = null,
    ): UserMutation {
        return UserMutation()
    }
}
