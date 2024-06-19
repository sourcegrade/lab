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
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.Users.clientDefault
import org.sourcegrade.lab.hub.domain.Submission
import java.util.UUID

internal object Submissions : UUIDTable("sgl_submissions") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val assignmentId = reference("assignment_id", Assignments)
    val submitterId = reference("submitter_id", Users)
    val groupId = reference("group_id", SubmissionGroups)
    val uploaded = timestamp("uploaded")
}

@GraphQLIgnore
internal class DBSubmission(id: EntityID<UUID>) : UUIDEntity(id), Submission {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Submissions.createdUtc
    override val assignment: DBAssignment by DBAssignment referencedOn Submissions.assignmentId
    override val submitter: DBUser by DBUser referencedOn Submissions.submitterId
    override val group: DBSubmissionGroup by DBSubmissionGroup referencedOn Submissions.groupId
    override val uploaded: Instant by Submissions.uploaded
    override val gradingRuns: SizedIterable<DBGradingRun> by DBGradingRun referrersOn GradingRuns.submissionId
    override val lastGradingRun: DBGradingRun?
        get() = DBGradingRun.find { GradingRuns.submissionId eq id }
            .orderBy(GradingRuns.createdUtc to SortOrder.DESC)
            .firstOrNull()

    companion object : EntityClass<UUID, DBSubmission>(Submissions)
}
