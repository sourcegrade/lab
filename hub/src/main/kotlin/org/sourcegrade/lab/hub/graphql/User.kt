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
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.repo.MutableUserRepository
import org.sourcegrade.lab.hub.domain.repo.UserRepository
import java.util.UUID

class UserQueries(private val repository: UserRepository) : Query {
    fun user(): UserQuery = UserQuery(repository)
}

@GraphQLDescription("Query user collection")
class UserQuery(private val repository: UserRepository) : UserRepository by repository {
    override suspend fun findById(id: UUID): User? = repository.findById(id)
    override suspend fun deleteById(id: UUID): Boolean = repository.deleteById(id)
    override suspend fun exists(id: UUID): Boolean = repository.exists(id)
    override suspend fun countAll(): Long = repository.countAll()
}

class UserMutations(
    private val logger: Logger,
    private val repository: MutableUserRepository,
) : Mutation {
    fun user(): UserMutation = UserMutation(logger, repository)
}

@GraphQLDescription("Mutation user collection")
class UserMutation(
    private val logger: Logger,
    private val repository: MutableUserRepository,
) {
    suspend fun create(dfe: DataFetchingEnvironment, item: User.CreateDto): User.Snapshot {
        logger.info("SelectionSet: ${dfe.selectionSet.fields.joinToString { it.name }}")
        return repository.create(item).toSnapshot()
    }
}