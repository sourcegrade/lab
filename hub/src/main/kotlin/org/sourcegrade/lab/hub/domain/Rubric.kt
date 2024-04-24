package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.sourcegrade.lab.hub.db.Criteria
import org.sourcegrade.lab.hub.db.Rubrics
import java.util.UUID

class Rubric(id: EntityID<UUID>) : UUIDEntity(id) {
    val minPoints: Int by Rubrics.minPoints
    val maxPoints: Int by Rubrics.maxPoints
    val allChildCriteria: SizedIterable<Criterion> by Criterion referrersOn Criteria.parentRubricId
    val childCriteria: SizedIterable<Criterion>
        get() = Criterion.find { Criteria.parentRubricId eq id and Criteria.parentCriterionId.isNull() }

    companion object : EntityClass<UUID, Rubric>(Rubrics)
}
