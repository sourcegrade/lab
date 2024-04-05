package org.sourcegrade.lab.hub.domain

import java.time.ZonedDateTime
import java.util.UUID

data class Submission(
    override val id: UUID,
    val assignment: Assignment,
    val submitter: User,
    val group: SubmissionGroup,
    val uploaded: ZonedDateTime,
    val gradingRuns: List<GradingRun>,
    val lastGradingRun: GradingRun,
) : DomainEntity
