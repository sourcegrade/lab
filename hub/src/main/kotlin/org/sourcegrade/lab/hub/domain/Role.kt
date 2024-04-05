package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class Role(
    override val id: UUID,
    val scope: String,
    val permissions: List<String>,
) : DomainEntity
