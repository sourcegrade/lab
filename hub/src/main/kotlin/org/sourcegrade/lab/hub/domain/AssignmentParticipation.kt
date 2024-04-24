package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable

interface AssignmentParticipation : DomainEntity {
    val assignment: Assignment
    val user: User
    suspend fun submissions(): SizedIterable<Submission>

    enum class Status {
        ALL,
        FUTURE,
        OPEN,
        NOT_SUBMITTED,
        SUBMITTED,
        EXPIRED,
    }
}
