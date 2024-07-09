/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.lab.hub.db

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.assignment.Assignments
import org.sourcegrade.lab.hub.db.assignment.DBAssignment
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Criterion
import org.sourcegrade.lab.hub.domain.Rubric
import java.util.UUID

internal object Rubrics : UUIDTable("sgl_rubrics") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val name = varchar("name", 255)
    val assignmentId = reference("assignment_id", Assignments)
    val maxPoints = integer("max_points")
    val minPoints = integer("min_points")
}

@GraphQLIgnore
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
