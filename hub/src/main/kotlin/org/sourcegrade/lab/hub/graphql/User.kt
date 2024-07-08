/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.lab.hub.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.db.SuspendedExecutionContext
import org.sourcegrade.lab.hub.db.UnconfinedExecutionContext
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserCollection
import org.sourcegrade.lab.hub.domain.repo.MutableUserRepository
import org.sourcegrade.lab.hub.domain.repo.UserRepository
import java.util.UUID

class UserQueries(
    private val logger: Logger,
    private val repository: UserRepository,
) : Query {
    fun user(): UserQuery = UserQuery(logger, repository)
}

@GraphQLDescription("Query user collection")
class UserQuery(
    private val logger: Logger,
    private val repository: UserRepository,
) {
    suspend fun findAll(): UserCollection {
        val context = SuspendedExecutionContext()
        val result = repository.findAll(context)
        context.execute()
        return result
    }

    suspend fun findById(dfe: DataFetchingEnvironment, id: UUID): User? = newSuspendedTransaction {
        repository.findById(id, UnconfinedExecutionContext, dfe.extractRelations())
    }

    suspend fun deleteById(id: UUID): Boolean = repository.deleteById(id)
    suspend fun exists(id: UUID): Boolean = repository.exists(id)
    suspend fun countAll(): Long = repository.countAll()

    suspend fun findByUsername(dfe: DataFetchingEnvironment, username: String): User? =
        repository.findByUsername(username, relations = dfe.extractRelations())

    suspend fun findAllByUsername(dfe: DataFetchingEnvironment, partialUsername: String): UserCollection {
        return repository.findAllByUsername(partialUsername)
    }
}

class UserMutations(
    private val logger: Logger,
    private val repository: MutableUserRepository,
) : Mutation {
    fun hello2(): String = "Hello, World!"
//    fun user(): UserMutation = UserMutation(logger, repository)
}

//@GraphQLDescription("Mutation user collection")
//class UserMutation(
//    private val logger: Logger,
//    private val repository: MutableUserRepository,
//) {
////    suspend fun create(dfe: DataFetchingEnvironment, item: User.CreateDto): User.Snapshot {
////        logger.info("SelectionSet: ${dfe.selectionSet.fields.joinToString { it.name }}")
////        return repository.create(item)
////    }
//}
