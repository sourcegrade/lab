package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.Criteria
import org.sourcegrade.lab.hub.db.Rubrics
import java.util.UUID

class Rubric(id: EntityID<UUID>) : UUIDEntity(id) {
    val childCriteria: SizedIterable<Criterion> by Criterion referrersOn Criteria.parentRubricId
    companion object : EntityClass<UUID, Rubric>(Rubrics)
}
