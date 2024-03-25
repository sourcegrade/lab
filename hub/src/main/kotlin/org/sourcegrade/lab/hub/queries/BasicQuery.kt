package org.sourcegrade.lab.hub.queries

import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.models.Model

class BasicQuery<T>(private val entityClass: EntityClass<String, Model<T>>) {
    suspend fun helloWorld(): String {
        return "Hello World"
    }
}

// generics not supported by gql :(
abstract class BasicMutation<T>(private val entityClass: EntityClass<String, Model<T>>) {
    protected fun requireId(environment: DataFetchingEnvironment): String {
        return environment.executionStepInfo.parent?.arguments?.get("id") as? String
            ?: throw IllegalArgumentException("id is required")
    }

    suspend fun fetch(environment: DataFetchingEnvironment): T {
        val id = requireId(environment)
        return newSuspendedTransaction {
            entityClass.findById(id)?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    suspend fun delete(environment: DataFetchingEnvironment): T {
        val id = requireId(environment)
        return newSuspendedTransaction {
            entityClass.findById(id)?.apply {
                delete()
            }?.toDTO() ?: throw IllegalArgumentException("No user with id $id found")
        }
    }

    abstract suspend fun create(
        environment: DataFetchingEnvironment,
        input: T,
    ): T
}
