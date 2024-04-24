package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable

interface Rubric : DomainEntity {
    val name: String
    val minPoints: Int
    val maxPoints: Int
    val assignment: Assignment
    val allChildCriteria: SizedIterable<Criterion>
    val childCriteria: SizedIterable<Criterion>
}
