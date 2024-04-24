package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.Criteria
import java.util.UUID

class Criterion(id: EntityID<UUID>) : UUIDEntity(id) {
    val parentRubric: Rubric by Rubric referencedOn Criteria.parentRubricId
    val parentCriterion: Criterion? by Criterion optionalReferencedOn Criteria.parentCriterionId
    val childCriteria: SizedIterable<Criterion> by Criterion optionalReferrersOn Criteria.parentCriterionId

    companion object : EntityClass<UUID, Criterion>(Criteria)
}
