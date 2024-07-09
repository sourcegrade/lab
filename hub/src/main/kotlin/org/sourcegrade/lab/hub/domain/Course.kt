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
import org.sourcegrade.lab.hub.graphql.extractRelations
import java.util.UUID

interface Course : TermScoped {
    val owner: User

    val name: String
    val description: String

    fun submissionGroupCategories(
        limit: DomainEntityCollection.Limit? = null,
        orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
    ): SubmissionGroupCategoryCollection

    fun assignments(
        limit: DomainEntityCollection.Limit? = null,
        orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
    ): AssignmentCollection

    data class CreateDto(
        val ownerUuid: UUID,
        val termUuid: UUID,
        val name: String,
        val description: OptionalInput<String>,
    ) : Creates<Course>
}

interface MutableCourse : Course {
    override var name: String
    override var description: String
}

interface CourseRepository : CollectionRepository<Course, CourseCollection> {

    suspend fun findByName(name: String, relations: List<Relation<Course>> = emptyList()): Course?

    suspend fun findAllByName(partialName: String): CourseCollection

    suspend fun findAllByDescription(partialDescription: String): CourseCollection // TODO: maybe instead CollectionRepo.search?

    suspend fun findAllByOwner(ownerId: UUID): CourseCollection
}

interface MutableCourseRepository : CourseRepository, MutableRepository<Course, Course.CreateDto>

interface CourseCollection : DomainEntityCollection<Course, CourseCollection> {
    override suspend fun count(): Long
    override suspend fun empty(): Boolean

    suspend fun list(dfe: DataFetchingEnvironment): List<Course> = list(dfe.extractRelations())
}
