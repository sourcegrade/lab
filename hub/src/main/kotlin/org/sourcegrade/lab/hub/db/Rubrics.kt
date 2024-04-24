package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object Rubrics : UUIDTable("sgl_rubrics") {
    val maxPoints = integer("max_points")
    val minPoints = integer("min_points")
}
