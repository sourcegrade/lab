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
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.domain.Criterion
import org.sourcegrade.lab.hub.domain.Rubric
import java.util.UUID

internal object Criteria : UUIDTable("sgl_criteria") {
    val parentRubricId = reference("parent_rubric_id", Rubrics)
    val parentCriterionId = reference("parent_criterion_id", Criteria).nullable()
    val minPoints = integer("minPoints")
    val maxPoints = integer("maxPoints")
    val description = text("description")
}

@GraphQLIgnore
class DBCriterion(id: EntityID<UUID>) : UUIDEntity(id), Criterion {
    override val uuid: UUID = id.value
    override val createdUtc: Instant
        get() = parentRubric.createdUtc
    override val minPoints: Int by Criteria.minPoints
    override val maxPoints: Int by Criteria.maxPoints
    override val description: String by Criteria.description
    override val parentRubric: Rubric by DBRubric referencedOn Criteria.parentRubricId
    override val parentCriterion: DBCriterion? by DBCriterion optionalReferencedOn Criteria.parentCriterionId
    override val childCriteria: SizedIterable<DBCriterion> by DBCriterion optionalReferrersOn Criteria.parentCriterionId

    companion object : EntityClass<UUID, DBCriterion>(Criteria)
}
