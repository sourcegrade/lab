package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID

interface UserMembership<T : TermScoped> : DomainEntity {
    val startUtc: Instant
    val endUtc: Instant?
    val user: User
    val target: T

    data class CreateDto<T : TermScoped>(
        val userId: UUID,
        val targetId: UUID,
        val startUtc: Instant? = null, // or else now
    ) : Creates<UserMembership<T>>

    enum class Status {
        ALL,
        FUTURE,
        CURRENT,
        PAST,
    }
}
