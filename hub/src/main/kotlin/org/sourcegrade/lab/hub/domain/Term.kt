package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant

interface Term : DomainEntity {
    val name: String
    val start: Instant
    val end: Instant?
}
