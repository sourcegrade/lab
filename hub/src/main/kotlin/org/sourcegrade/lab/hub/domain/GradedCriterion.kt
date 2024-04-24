package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.GradedCriteria
import java.util.UUID

class GradedCriterion(id: EntityID<UUID>) : Entity<UUID>(id) {
    val parentGradedRubric: GradedRubric by GradedRubric referencedOn GradedCriteria.parentGradedRubricId
    val parentGradedCriterion: GradedCriterion? by GradedCriterion optionalReferencedOn GradedCriteria.parentGradedCriterionId
    val criterion: Criterion by Criterion referencedOn GradedCriteria.criterionId
    val achievedMinPoints: Int by GradedCriteria.achievedMinPoints
    val achievedMaxPoints: Int by GradedCriteria.achievedMaxPoints
    val message: String by GradedCriteria.message

    companion object : EntityClass<UUID, GradedCriterion>(GradedCriteria)
}
