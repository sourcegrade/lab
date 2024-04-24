package org.sourcegrade.lab.hub.domain

import kotlin.time.Duration

interface GradingRun : DomainEntity {
    val submission: Submission
    val gradedRubric: GradedRubric
    val runtime: Duration

    val rubric: Rubric
    val minPoints: Int
    val maxPoints: Int
    val achievedMinPoints: Int
    val achievedMaxPoints: Int
}
