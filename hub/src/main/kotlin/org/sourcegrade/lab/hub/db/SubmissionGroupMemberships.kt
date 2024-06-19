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

package org.sourcegrade.lab.hub.db

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.SubmissionGroup
import org.sourcegrade.lab.hub.domain.UserMembership
import java.util.UUID

internal object SubmissionGroupMemberships : UUIDTable("sgl_submission_group_membership") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val startUtc = timestamp("startUtc")
    val endUtc = timestamp("endUtc").nullable()
    val userId = reference("user_id", Users)
    val submissionGroupId = reference("submission_group_id", SubmissionGroups)
}

@GraphQLIgnore
internal class DBSubmissionGroupMembership(id: EntityID<UUID>) : UUIDEntity(id), UserMembership<SubmissionGroup> {
    override val uuid: UUID = id.value
    override val createdUtc by SubmissionGroupMemberships.createdUtc
    override val startUtc by SubmissionGroupMemberships.startUtc
    override val endUtc by SubmissionGroupMemberships.endUtc
    override val user: DBUser by DBUser referencedOn SubmissionGroupMemberships.userId
    override val target: DBSubmissionGroup by DBSubmissionGroup referencedOn SubmissionGroupMemberships.submissionGroupId

    companion object : EntityClass<UUID, DBSubmissionGroupMembership>(SubmissionGroupMemberships)
}
