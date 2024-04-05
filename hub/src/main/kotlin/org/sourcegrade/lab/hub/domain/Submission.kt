package org.sourcegrade.lab.hub.domain

import org.sourcegrade.lab.hub.db.DomainEntity
import java.time.ZonedDateTime
import java.util.UUID

data class Submission(
    override val id: UUID,
    val assignment: Assignment,
    val submitter: User,
    val uploaded: ZonedDateTime,
    val gradingRuns: List<GradingRun>,
    val lastGradingRun: GradingRun,
) : DomainEntity
