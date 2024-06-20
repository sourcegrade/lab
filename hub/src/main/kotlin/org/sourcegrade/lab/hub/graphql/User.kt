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
import org.sourcegrade.lab.hub.domain.UserCollection
import org.sourcegrade.lab.hub.domain.repo.MutableUserRepository
import org.sourcegrade.lab.hub.domain.repo.UserRepository

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

    suspend fun findAll(dfe: DataFetchingEnvironment): UserCollection {
        val relations = dfe.selectionSet.fields.map { it.name }
//        logger.info("Relations: $relations")


        dfe.selectionSet.immediateFields
        return repository.findAll()
    }

//    suspend fun findById(id: UUID): User? = repository.findById(id)

    //    suspend fun deleteById(id: UUID): Boolean = repository.deleteById(id)
//    suspend fun exists(id: UUID): Boolean = repository.exists(id)
//    suspend fun countAll(): Long = repository.countAll()
    fun hello1(): String = "Hello, World!"
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
