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

@GraphQLIgnore
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
