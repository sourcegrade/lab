package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

interface Submission : DomainEntity {
    val assignment: Assignment
    val submitter: User
    val group: SubmissionGroup
    val uploaded: Instant
    val gradingRuns: SizedIterable<GradingRun>
    val lastGradingRun: GradingRun?

    enum class Status {
        ALL,
        PENDING_GRADE,
        GRADED,
    }

    data class CreateDto(
        val assignmentId: UUID,
        val bytes: ByteArray,
    ) : Creates<Submission>
}
