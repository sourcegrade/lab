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

package org.sourcegrade.lab.hub.domain

import com.expediagroup.graphql.generator.execution.OptionalInput
import graphql.schema.DataFetchingEnvironment
import kotlinx.datetime.Instant
import org.sourcegrade.lab.hub.graphql.extractRelations

interface User : DomainEntity {
    val email: String
    val username: String
    val displayname: String

    suspend fun assignments(
        term: OptionalInput<String>,
        now: OptionalInput<Instant>,
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): AssignmentCollection

    data class CreateUserDto(
        val email: String,
        val username: String,
        val displayname: OptionalInput<String> = OptionalInput.Defined(username),
    ) : Creates<User>
}

interface MutableUser : User {
    override var email: String
    override var username: String
    override var displayname: String
}

interface UserRepository : CollectionRepository<User, UserCollection> {
    suspend fun findByUsername(username: String, relations: List<Relation<User>> = emptyList()): User?
    suspend fun findByEmail(email: String, relations: List<Relation<User>> = emptyList()): User?
    suspend fun findAllByUsername(partialUsername: String): UserCollection
}

interface MutableUserRepository : UserRepository, MutableRepository<User, User.CreateUserDto>

interface UserCollection : DomainEntityCollection<User, UserCollection> {
    override suspend fun count(): Long
    override suspend fun empty(): Boolean
    suspend fun list(dfe: DataFetchingEnvironment): List<User> = list(dfe.extractRelations())
}
