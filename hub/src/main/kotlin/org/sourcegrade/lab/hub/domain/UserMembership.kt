package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable

interface UserMembership<T : TermScoped> : DomainEntity {
    val startUtc: Instant
    val endUtc: Instant?
    val user: User
    val target: T

    interface Accessor<T : TermScoped> {
        suspend fun all(): SizedIterable<UserMembership<T>>
        suspend fun current(): SizedIterable<UserMembership<T>>
        suspend fun active(): SizedIterable<UserMembership<T>>
    }
}
