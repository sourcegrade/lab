package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object Rubrics : UUIDTable("sgl_rubrics") {
    val createdUtc = timestamp("createdUtc")
    val maxPoints = integer("max_points")
    val minPoints = integer("min_points")
}
