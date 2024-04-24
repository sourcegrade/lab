package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object GradingRuns : UUIDTable("sgl_grading_runs") {
    val createdUtc = timestamp("createdUtc")
    val submissionId = reference("submission_id", Submissions)
    val gradedRubricId = reference("graded_rubric_id", GradedRubrics)
    val rubricId = reference("rubric_id", Rubrics)
    val minPoints = integer("min_points")
    val maxPoints = integer("max_points")
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
}
