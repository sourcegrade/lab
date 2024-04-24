package org.sourcegrade.lab.hub.domain

interface GradedCriterion : DomainEntity {
    val parentGradedRubric: GradedRubric
    val parentGradedCriterion: GradedCriterion?
    val criterion: Criterion
    val achievedMinPoints: Int
    val achievedMaxPoints: Int
    val message: String
}
