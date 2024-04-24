package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.Submission
import java.util.UUID

internal object Submissions : UUIDTable("sgl_submissions") {
    val createdUtc = timestamp("createdUtc")
    val assignmentId = reference("assignment_id", Assignments)
    val submitterId = reference("submitter_id", Users)
    val groupId = reference("group_id", SubmissionGroups)
    val uploaded = timestamp("uploaded")
}

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
