package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID

interface Assignment : DomainEntity {
    val course: Course
    val submissionGroupCategory: SubmissionGroupCategory

    var name: String
    var description: String
    var submissionDeadlineUtc: Instant

    suspend fun setSubmissionGroupCategoryId(id: UUID): Boolean

    data class CreateDto(
        val courseId: UUID,
        val submissionGroupCategoryId: UUID,
        val name: String,
        val description: String,
        val submissionDeadlineUtc: Instant,
    ) : Creates<Assignment>
}
