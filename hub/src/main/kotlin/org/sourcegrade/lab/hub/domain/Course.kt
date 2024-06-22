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

import com.expediagroup.graphql.generator.execution.OptionalInput
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.graphql.extractRelations
import java.util.UUID

interface Course : TermScoped {
    val submissionGroupCategories: SizedIterable<SubmissionGroupCategory>
    val assignments: SizedIterable<Assignment>
    val owner: User

    val name: String
    val description: String

    data class CreateDto(
        val ownerUUID: UUID,
        val name: String,
        val description: OptionalInput<String>,
    ) : Creates<Course>
}

interface MutableCourse : Course {
    override var name: String
    override var description: String
}

interface CourseCollection : DomainEntityCollection<Course, CourseCollection> {
    override fun limit(num: Int, offset: Long): CourseCollection
    override fun page(page: Int, pageSize: Int): CourseCollection
    override fun orderBy(orders: List<DomainEntityCollection.FieldOrdering>): CourseCollection
    override suspend fun count(): Long
    override suspend fun empty(): Boolean

    suspend fun list(dfe: DataFetchingEnvironment): List<Course> = list(dfe.extractRelations())
}
