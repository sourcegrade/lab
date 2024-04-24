package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object GradedRubrics : UUIDTable("sgl_graded_rubrics") {
    val rubricId = reference("rubric_id", Rubrics)
    val name = varchar("name", 255)
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
}
