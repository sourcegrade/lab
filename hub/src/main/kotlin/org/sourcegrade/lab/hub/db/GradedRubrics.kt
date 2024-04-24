package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.GradedRubric
import java.util.UUID

internal object GradedRubrics : UUIDTable("sgl_graded_rubrics") {
    val createdUtc = timestamp("created_utc")
    val rubricId = reference("rubric_id", Rubrics)
    val name = varchar("name", 255)
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
}

class DBGradedRubric(id: EntityID<UUID>) : Entity<UUID>(id), GradedRubric {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by GradedRubrics.createdUtc
    override val rubric: DBRubric by DBRubric referencedOn GradedRubrics.rubricId
    override val name: String by GradedRubrics.name
    override val achievedMinPoints: Int by GradedRubrics.achievedMinPoints
    override val achievedMaxPoints: Int by GradedRubrics.achievedMaxPoints
    override val allChildCriteria: SizedIterable<DBGradedCriterion> by DBGradedCriterion referrersOn GradedCriteria.parentGradedRubricId
    override val childCriteria: SizedIterable<DBGradedCriterion>
        get() = DBGradedCriterion.find { (GradedCriteria.parentGradedRubricId eq id) and GradedCriteria.parentGradedCriterionId.isNull() }

    companion object : EntityClass<UUID, DBGradedRubric>(GradedRubrics)
}
