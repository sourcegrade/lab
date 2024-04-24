package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.GradedRubrics
import org.sourcegrade.lab.hub.db.GradingRuns
import java.util.UUID

//data class GradingRun(
//    override val id: UUID,
//    val maxPoints: Int,
//    val minPoints: Int,
//    val rubric: ByteArray,
//) : DomainEntity

class GradingRun(id: EntityID<UUID>) : UUIDEntity(id) {
    val submission: Submission by Submission referencedOn GradingRuns.submissionId
//    val achievedMinPoints: Int by GradingRuns.achievedMinPoints
//    val achievedMaxPoints: Int by GradingRuns.achievedMaxPoints
    val rubric: Rubric by Rubric referencedOn GradingRuns.rubricId
    val gradedRubric: GradedRubric by GradedRubric referencedOn GradingRuns.gradedRubricId

    companion object : EntityClass<UUID, GradingRun>(GradingRuns)
}
