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

package org.sourcegrade.lab.hub.domain.repo

import org.sourcegrade.lab.hub.db.UnconfinedExecutionContext
import org.sourcegrade.lab.hub.domain.ExecutionContext
import org.sourcegrade.lab.hub.domain.Relation
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserCollection

interface UserRepository : CollectionRepository<User, UserCollection> {

    suspend fun findByUsername(
        username: String,
        context: ExecutionContext = UnconfinedExecutionContext,
        relations: List<Relation<User>> = emptyList(),
    ): User?

    suspend fun findAllByUsername(
        partialUsername: String,
        context: ExecutionContext = UnconfinedExecutionContext,
    ): UserCollection

    suspend fun findByEmail(
        email: String,
        context: ExecutionContext = UnconfinedExecutionContext,
        relations: List<Relation<User>> = emptyList(),
    ): User?
}

interface MutableUserRepository : UserRepository, MutableRepository<User, User.CreateDto>
