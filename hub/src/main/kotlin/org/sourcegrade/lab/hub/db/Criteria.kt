package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object Criteria : UUIDTable("sgl_criteria") {
    val parentRubricId = reference("parent_rubric_id", Rubrics)
    val parentCriterionId = reference("parent_criterion_id", Criteria).nullable()
    val minPoints = integer("minPoints")
    val maxPoints = integer("maxPoints")
    val description = text("description")
}
