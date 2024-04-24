package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.GradingRuns
import org.sourcegrade.lab.hub.db.Submissions
import java.util.UUID

class Submission(id: EntityID<UUID>) : UUIDEntity(id) {
    val assignment by Assignment referencedOn Submissions.assignmentId
    val submitter by User referencedOn Submissions.submitterId
    val group by SubmissionGroup referencedOn Submissions.groupId
    val uploaded by Submissions.uploaded
    val gradingRuns by GradingRun referrersOn GradingRuns.submissionId
    // TODO: lastGradingRun

    companion object : EntityClass<UUID, Submission>(Submissions)
}
