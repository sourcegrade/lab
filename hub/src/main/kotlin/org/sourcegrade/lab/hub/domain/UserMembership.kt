package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant

interface UserMembership<T : TermScoped> : DomainEntity {
    val startUtc: Instant
    val endUtc: Instant?
    val user: User
    val target: T

    enum class Status {
        ALL,
        FUTURE,
        CURRENT,
        PAST,
    }
}
