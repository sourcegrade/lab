package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.SubmissionTable
import java.util.UUID

//data class Submission(
//    override val id: UUID,
//    val assignment: Assignment,
//    val submitter: User,
//    val group: SubmissionGroup,
//    val uploaded: ZonedDateTime,
//    val gradingRuns: List<GradingRun>,
//    val lastGradingRun: GradingRun,
//) : DomainEntity

class Submission(id: EntityID<UUID>) : UUIDEntity(id) {
    val assignment by Assignment referencedOn SubmissionTable.assignmentId
    val submitter by User referencedOn SubmissionTable.submitterId
    val group by SubmissionGroup referencedOn SubmissionTable.groupId

    companion object : EntityClass<UUID, Submission>(SubmissionTable)
}
