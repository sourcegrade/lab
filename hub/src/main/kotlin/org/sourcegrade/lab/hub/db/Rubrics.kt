package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Criterion
import org.sourcegrade.lab.hub.domain.Rubric
import java.util.UUID

internal object Rubrics : UUIDTable("sgl_rubrics") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255)
    val assignmentId = reference("assignment_id", Assignments)
    val maxPoints = integer("max_points")
    val minPoints = integer("min_points")
}

class DBRubric(id: EntityID<UUID>) : UUIDEntity(id), Rubric {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Rubrics.createdUtc
    override var name: String by Rubrics.name
    override val minPoints: Int by Rubrics.minPoints
    override val maxPoints: Int by Rubrics.maxPoints
    override val assignment: Assignment by DBAssignment referencedOn Rubrics.assignmentId
    override val allChildCriteria: SizedIterable<Criterion> by DBCriterion referrersOn Criteria.parentRubricId
    override val childCriteria: SizedIterable<Criterion>
        get() = DBCriterion.find { (Criteria.parentRubricId eq id) and Criteria.parentCriterionId.isNull() }

    companion object : EntityClass<UUID, DBRubric>(Rubrics)
}
