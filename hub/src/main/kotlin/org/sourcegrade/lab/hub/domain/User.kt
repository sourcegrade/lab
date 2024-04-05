package org.sourcegrade.lab.hub.domain

import org.sourcegrade.lab.hub.db.DomainEntity
import java.util.UUID

data class User(
    override val id: UUID,
    val username: String,
    val email: String? = null,
    val roles: List<Role>,
) : DomainEntity
