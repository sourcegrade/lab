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

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

interface UserMembership<T : TermScoped> : DomainEntity {
    val startUtc: Instant
    val endUtc: Instant?
    val user: User
    val target: T

    data class CreateDto<T : TermScoped>(
        val userId: UUID,
        val targetId: UUID,
        val startUtc: Instant? = null, // or else now
    ) : Creates<UserMembership<T>>

    enum class UserMembershipStatus {
        ALL,
        FUTURE,
        CURRENT,
        PAST,
    }
}

interface UserMembershipRepository<T : TermScoped> : Repository<UserMembership<T>> {
    suspend fun find(
        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<T>
}

interface MutableUserMembershipRepository<T : TermScoped> : UserMembershipRepository<T>,
    MutableRepository<UserMembership<T>, UserMembership.CreateDto<T>>
