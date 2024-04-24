package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID

interface Assignment : DomainEntity {
    val course: Course
    val submissionGroupCategory: SubmissionGroupCategory

    var name: String
    var description: String
    var submissionDeadlineUtc: Instant
    var submissionGroupCategoryId: UUID
}
