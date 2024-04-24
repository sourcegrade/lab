package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable

interface GradedRubric : DomainEntity {
    val rubric: Rubric
    val name: String
    val achievedMinPoints: Int
    val achievedMaxPoints: Int
    val allChildCriteria: SizedIterable<GradedCriterion>
    val childCriteria: SizedIterable<GradedCriterion>
}
