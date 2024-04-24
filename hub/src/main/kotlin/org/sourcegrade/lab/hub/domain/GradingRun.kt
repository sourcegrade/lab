package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.GradingRuns
import java.util.UUID

class GradingRun(id: EntityID<UUID>) : UUIDEntity(id) {
    val createdUtc: Instant by GradingRuns.createdUtc
    val submission: Submission by Submission referencedOn GradingRuns.submissionId
    val gradedRubric: GradedRubric by GradedRubric referencedOn GradingRuns.gradedRubricId

    // duplicated data from GradedRubric to minimize joins for frequent queries
    val rubric: Rubric by Rubric referencedOn GradingRuns.rubricId
    val minPoints: Int by GradingRuns.minPoints
    val maxPoints: Int by GradingRuns.maxPoints
    val achievedMinPoints: Int by GradingRuns.achievedMinPoints
    val achievedMaxPoints: Int by GradingRuns.achievedMaxPoints

    companion object : EntityClass<UUID, GradingRun>(GradingRuns)
}
