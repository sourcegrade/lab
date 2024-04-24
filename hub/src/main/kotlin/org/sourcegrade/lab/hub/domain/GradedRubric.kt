package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.sourcegrade.lab.hub.db.GradedCriteria
import org.sourcegrade.lab.hub.db.GradedRubrics
import java.util.UUID

class GradedRubric(id: EntityID<UUID>) : Entity<UUID>(id) {
    val rubric: Rubric by Rubric referencedOn GradedRubrics.rubricId
    val name: String by GradedRubrics.name
    val achievedMinPoints: Int by GradedRubrics.achievedMinPoints
    val achievedMaxPoints: Int by GradedRubrics.achievedMaxPoints
    val allChildCriteria: SizedIterable<GradedCriterion> by GradedCriterion referrersOn GradedCriteria.parentGradedRubricId
    val childCriteria: SizedIterable<GradedCriterion>
        get() = GradedCriterion.find { GradedCriteria.parentGradedRubricId eq id and GradedCriteria.parentGradedCriterionId.isNull() }

    companion object : EntityClass<UUID, GradedRubric>(GradedRubrics)
}
