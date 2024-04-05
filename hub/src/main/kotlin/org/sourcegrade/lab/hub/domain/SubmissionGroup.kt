package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class SubmissionGroup(
    override val id: UUID,
    val users: List<User>,
    val category: SubmissionGroupCategory,
) : DomainEntity
