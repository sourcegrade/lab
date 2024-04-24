package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.sourcegrade.lab.hub.domain.GradedCriterion
import org.sourcegrade.lab.hub.domain.GradedRubric
import java.util.UUID

object GradedCriteria : UUIDTable("sgl_graded_criteria") {
    val parentGradedRubricId = reference("parent_graded_rubric_id", GradedRubrics)
    val parentGradedCriterionId = reference("parent_graded_criterion_id", GradedCriteria).nullable()
    val criterionId = reference("criterion_id", Criteria)
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
    val message = text("message")
}

class DBGradedCriterion(id: EntityID<UUID>) : Entity<UUID>(id), GradedCriterion {
    override val uuid: UUID = id.value
    override val createdUtc: Instant
        get() = parentGradedRubric.createdUtc
    override val parentGradedRubric: GradedRubric by DBGradedRubric referencedOn GradedCriteria.parentGradedRubricId
    override val parentGradedCriterion: DBGradedCriterion? by DBGradedCriterion optionalReferencedOn GradedCriteria.parentGradedCriterionId
    override val criterion: DBCriterion by DBCriterion referencedOn GradedCriteria.criterionId
    override val achievedMinPoints: Int by GradedCriteria.achievedMinPoints
    override val achievedMaxPoints: Int by GradedCriteria.achievedMaxPoints
    override val message: String by GradedCriteria.message

    companion object : EntityClass<UUID, DBGradedCriterion>(GradedCriteria)
}
