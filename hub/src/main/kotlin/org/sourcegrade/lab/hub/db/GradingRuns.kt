package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object GradingRuns : UUIDTable("sgl_grading_runs") {
    val submissionId = reference("submission_id", Submissions)
    val rubricId = reference("rubric_id", Rubrics)
    val gradedRubricId = reference("graded_rubric_id", GradedRubrics)
    val minPoints = integer("minPoints")
    val maxPoints = integer("maxPoints")
}
