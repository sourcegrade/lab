package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class User(
    override val id: UUID,
    val username: String,
    val email: String,
) : DomainEntity {
    data class CreateDto(
        val username: String,
        val email: String,
    ) : Creates<User>
}
