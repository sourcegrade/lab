package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object GradingRunTable : UUIDTable("sgl_grading_runs") {
    val maxPoints = integer("max_points")
    val minPoints = integer("min_points")
    // TODO: rubric = ...
}
