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

import graphql.schema.DataFetchingEnvironment
import org.sourcegrade.lab.hub.graphql.extractRelations

interface User : DomainEntity {
    val email: String
    val username: String
    val displayname: String

    data class CreateDto(
        val email: String,
        val username: String,
        val displayname: String = username,
    ) : Creates<User>
}

interface MutableUser : User {
    override var email: String
    override var username: String
    override var displayname: String
}

interface UserCollection : DomainEntityCollection<User, UserCollection> {
    override fun limit(num: Int, offset: Long): UserCollection
    override fun page(page: Int, pageSize: Int): UserCollection
    override fun orderBy(orders: List<DomainEntityCollection.FieldOrdering>): UserCollection
    override suspend fun count(): Long
    override suspend fun empty(): Boolean

    suspend fun list(dfe: DataFetchingEnvironment): List<User> = list(dfe.extractRelations())
}
