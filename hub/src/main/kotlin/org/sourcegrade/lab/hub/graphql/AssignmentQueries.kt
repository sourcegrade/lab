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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.apache.logging.log4j.Logger
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.AssignmentCollection
import org.sourcegrade.lab.hub.domain.AssignmentRepository
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableAssignmentRepository
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Term
import java.util.UUID

class AssignmentQueries(
    private val logger: Logger,
    private val repository: AssignmentRepository,
) : Query {
    fun assignment(): AssignmentQuery = AssignmentQuery(logger, repository)
}

class AssignmentQuery(
    private val logger: Logger,
    private val repository: AssignmentRepository,
) {
    suspend fun findAll(
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): AssignmentCollection = repository.findAll(limit = limit.flatten(), orders = orders.flattenList())

    suspend fun findById(dfe: DataFetchingEnvironment, id: UUID): Assignment? = repository.findById(id, dfe.extractRelations())
    suspend fun deleteById(id: UUID): Boolean = repository.deleteById(id)
    suspend fun exists(id: UUID): Boolean = repository.exists(id)
    suspend fun countAll(): Long = repository.countAll()

    suspend fun findAllByCourse(
        courseId: UUID,
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): AssignmentCollection = repository.findAllByCourse(courseId, limit.flatten(), orders.flattenList())

    suspend fun findAllByName(
        partialName: String,
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): AssignmentCollection = repository.findAllByName(partialName, limit.flatten(), orders.flattenList())

    suspend fun findAllByUser(
        userId: UUID,
        term: OptionalInput<String>,
        now: OptionalInput<Instant>,
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): AssignmentCollection = repository.findAllByUser(
        userId,
        term = Term.Matcher.fromNullableString(term.flatten()),
        now = now.flatten { Clock.System.now() },
        limit = limit.flatten(),
        orders = orders.flattenList(),
    )
}

class AssignmentMutations(
    private val logger: Logger,
    private val repository: MutableAssignmentRepository,
) : Mutation {
    fun assignment(): AssignmentMutation = AssignmentMutation(logger, repository)
}

class AssignmentMutation(
    private val logger: Logger,
    private val repository: MutableAssignmentRepository,
) {
    suspend fun createAssignment(dfe: DataFetchingEnvironment, input: Assignment.CreateAssignmentDto): Assignment =
        repository.create(input, dfe.extractRelations())

    suspend fun put(dfe: DataFetchingEnvironment, item: Assignment.CreateAssignmentDto): AssignmentPutResult =
        repository.put(item, dfe.extractRelations()).convert()

    data class AssignmentPutResult(val entity: Assignment, val created: Boolean)

    private fun MutableRepository.PutResult<Assignment>.convert(): AssignmentPutResult = AssignmentPutResult(entity, created)
}
