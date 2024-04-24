package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable

interface Criterion : DomainEntity {
    val description: String
    val minPoints: Int
    val maxPoints: Int
    val parentRubric: Rubric
    val parentCriterion: Criterion?
    val childCriteria: SizedIterable<Criterion>
}
