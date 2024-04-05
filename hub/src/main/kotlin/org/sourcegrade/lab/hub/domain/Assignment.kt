package org.sourcegrade.lab.hub.domain

import java.time.ZonedDateTime
import java.util.UUID

data class Assignment(
    override val id: UUID,
    val name: String,
    val description: String,
    val course: Course,
    val submissionDeadline: ZonedDateTime,
    val submissionGroupCategory: SubmissionGroupCategory,
) : DomainEntity
