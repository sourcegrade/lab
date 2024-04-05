package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class SubmissionGroupCategory(
    override val id: UUID,
    val name: String,
    val minSize: Int,
    val maxSize: Int,
) : DomainEntity
