package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class Course(
    override val id: UUID,
    val name: String,
    val description: String,
    val term: Term,
    val owner: User,
    val submissionGroupCategories: List<SubmissionGroup.Category>,
    val assignments: List<Assignment>,
) : DomainEntity
