package org.sourcegrade.lab.hub.domain

import org.sourcegrade.lab.hub.db.DomainEntity
import java.time.ZonedDateTime
import java.util.UUID

data class Assignment(
    override val id: UUID,
    val name: String,
    val description: String,
    val course: Course,
    val submissionDeadline: ZonedDateTime,
) : DomainEntity
