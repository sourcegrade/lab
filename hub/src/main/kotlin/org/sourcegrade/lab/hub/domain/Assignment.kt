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

package org.sourcegrade.lab.hub.domain

import graphql.schema.DataFetchingEnvironment
import kotlinx.datetime.Instant
import org.sourcegrade.lab.hub.graphql.extractRelations
import java.util.UUID

interface Assignment : DomainEntity {
    val course: Course
    val submissionGroupCategory: SubmissionGroupCategory

    val name: String
    val description: String
    val submissionDeadlineUtc: Instant

    data class CreateDto(
        val courseId: UUID,
        val submissionGroupCategoryId: UUID,
        val name: String,
        val description: String,
        val submissionDeadlineUtc: Instant,
    ) : Creates<Assignment>
}

interface MutableAssignment : Assignment {
    override var name: String
    override var description: String
    override var submissionDeadlineUtc: Instant
}

interface AssignmentCollection : DomainEntityCollection<Assignment, AssignmentCollection> {
    override fun limit(num: Int, offset: Long): AssignmentCollection
    override fun orderBy(orders: List<DomainEntityCollection.FieldOrdering>): AssignmentCollection
    override suspend fun count(): Long
    override suspend fun empty(): Boolean

    suspend fun list(dfe: DataFetchingEnvironment): List<Assignment> = list(dfe.extractRelations())
}
