package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID

interface Term : DomainEntity {
    val name: String
    val start: Instant
    val end: Instant?

    sealed interface Matcher {
        data object All : Matcher
        data object Current : Matcher
        data class ByName(val name: String) : Matcher
        data class ById(val id: UUID) : Matcher
    }
}
