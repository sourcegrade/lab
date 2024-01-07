package org.sourcegrade.yougrade.hub.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.yougrade.hub.models.Course
import org.sourcegrade.yougrade.hub.models.CourseDTO
import org.sourcegrade.yougrade.hub.models.SemesterType
import org.sourcegrade.yougrade.hub.models.UserDTO

@GraphQLDescription("Query collection for Courses")
class CourseQuery {
    private fun requireId(environment: DataFetchingEnvironment): String {
        return environment.executionStepInfo.parent?.arguments?.get("id") as? String
            ?: throw IllegalArgumentException("id is required")
    }

    @GraphQLDescription("Get a list of all Courses")
    suspend fun fetchAll(): List<CourseDTO> {
        return newSuspendedTransaction {
            Course.all().map { it.toDTO() }
        }
    }

    @GraphQLDescription("Get a single Course by id")
    suspend fun fetch(environment: DataFetchingEnvironment): CourseDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            Course.findById(id)?.toDTO() ?: throw IllegalArgumentException("No Course with id $id found")
        }
    }

    @GraphQLDescription("Get the members of a Course by id")
    suspend fun members(environment: DataFetchingEnvironment): List<UserDTO> {
        val id = requireId(environment)
        return newSuspendedTransaction {
            Course.findById(id)?.members?.toList()?.map { it.toDTO() } ?: throw IllegalArgumentException("No Course with id $id found")
        }
    }
}

class CourseQueries : Query {
    suspend fun course(
        environment: DataFetchingEnvironment,
        id: String? = null,
    ): CourseQuery {
        return CourseQuery()
    }
}

@GraphQLDescription("Mutation collection for Courses")
class CourseMutation {
    private fun requireId(environment: DataFetchingEnvironment): String {
        return environment.executionStepInfo.parent?.arguments?.get("id") as? String
            ?: throw IllegalArgumentException("id is required")
    }

    data class CreateCourseInput(val name: String, val description: String, val semesterType: SemesterType, val semesterStartYear: Int)

    @GraphQLDescription("Create a new Course")
    suspend fun create(
        environment: DataFetchingEnvironment,
        input: CreateCourseInput,
    ): CourseDTO {
        return newSuspendedTransaction {
            Course.new {
                this.name = input.name
                this.description = input.description
                this.semesterType = input.semesterType
                this.semesterStartYear = input.semesterStartYear
            }.toDTO()
        }
    }

    @GraphQLDescription("Delete a Course by id")
    suspend fun delete(environment: DataFetchingEnvironment): CourseDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            Course.findById(id)?.apply {
                delete()
            }?.toDTO() ?: throw IllegalArgumentException("No Course with id $id found")
        }
    }

    @GraphQLDescription("Get a single Course by id")
    suspend fun fetch(environment: DataFetchingEnvironment): CourseDTO {
        val id = requireId(environment)
        return newSuspendedTransaction {
            Course.findById(id)?.toDTO() ?: throw IllegalArgumentException("No Course with id $id found")
        }
    }

    @GraphQLDescription("Update a Course's Name")
    suspend fun updateName(
        environment: DataFetchingEnvironment,
        @GraphQLName("name") newName: String,
    ): String {
        val id = requireId(environment)
        return newSuspendedTransaction {
            Course.findById(id)?.apply {
                name = newName
            }?.toDTO() ?: throw IllegalArgumentException("No Course with id $id found")
        }.name
    }
}

class CourseMutations : Mutation {
    suspend fun course(
        environment: DataFetchingEnvironment,
        id: String? = null,
    ): CourseMutation {
        return CourseMutation()
    }
}
