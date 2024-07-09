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

package org.sourcegrade.lab.hub.graphql

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.apache.logging.log4j.Logger
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.CourseCollection
import org.sourcegrade.lab.hub.domain.CourseRepository
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableCourseRepository
import org.sourcegrade.lab.hub.domain.MutableRepository
import java.util.UUID

class CourseQueries(
    private val logger: Logger,
    private val repository: CourseRepository,
) : Query {
    fun course(): CourseQuery = CourseQuery(logger, repository)
}

class CourseQuery(
    private val logger: Logger,
    private val repository: CourseRepository,
) {
    suspend fun findAll(
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): CourseCollection = repository.findAll(limit.flatten(), orders.flattenList())

    suspend fun findById(dfe: DataFetchingEnvironment, id: UUID): Course? = repository.findById(id, dfe.extractRelations())
    suspend fun exists(id: UUID): Boolean = repository.exists(id)
    suspend fun countAll(): Long = repository.countAll()
}

class CourseMutations(
    private val logger: Logger,
    private val repository: MutableCourseRepository,
) : Mutation {
    fun course(): CourseMutation = CourseMutation(logger, repository)
}

class CourseMutation(
    private val logger: Logger,
    private val repository: MutableCourseRepository,
) {
    suspend fun create(dfe: DataFetchingEnvironment, item: Course.CreateDto): Course =
        repository.create(item, dfe.extractRelations())

    suspend fun put(dfe: DataFetchingEnvironment, item: Course.CreateDto): PutResult =
        repository.put(item, dfe.extractRelations()).convert()

    data class PutResult(val entity: Course, val created: Boolean)

    private fun MutableRepository.PutResult<Course>.convert(): PutResult = PutResult(entity, created)
}
