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
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val rubricId = reference("rubric_id", Rubrics)
    val name = varchar("name", 255)
    val achievedMinPoints = integer("achieved_min_points")
    val achievedMaxPoints = integer("achieved_max_points")
}

@GraphQLIgnore
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
