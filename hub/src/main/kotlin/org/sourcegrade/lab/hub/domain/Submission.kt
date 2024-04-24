package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.sourcegrade.lab.hub.db.GradingRuns
import org.sourcegrade.lab.hub.db.Submissions
import java.util.UUID

class Submission(id: EntityID<UUID>) : UUIDEntity(id) {
    val createdUtc: Instant by Submissions.createdUtc
    val assignment: Assignment by Assignment referencedOn Submissions.assignmentId
    val submitter: User by User referencedOn Submissions.submitterId
    val group: SubmissionGroup by SubmissionGroup referencedOn Submissions.groupId
    val uploaded: Instant by Submissions.uploaded
    val gradingRuns: SizedIterable<GradingRun> by GradingRun referrersOn GradingRuns.submissionId
    val lastGradingRun: GradingRun?
        get() = GradingRun.find { GradingRuns.submissionId eq id }
            .orderBy(GradingRuns.createdUtc to SortOrder.DESC)
            .firstOrNull()

    companion object : EntityClass<UUID, Submission>(Submissions)
}
