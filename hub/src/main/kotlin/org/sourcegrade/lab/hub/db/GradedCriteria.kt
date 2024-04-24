package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

object GradedCriteria : UUIDTable("sgl_graded_criteria") {
    val parentGradedRubricId = reference("parent_graded_rubric_id", GradedRubrics)
    val parentGradedCriterionId = reference("parent_graded_criterion_id", GradedCriteria).nullable()
    val criterionId = reference("criterion_id", Criteria)
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
    val message = text("message")
}
