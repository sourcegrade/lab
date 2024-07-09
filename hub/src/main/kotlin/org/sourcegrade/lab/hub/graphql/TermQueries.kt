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
import graphql.schema.DataFetchingEnvironment
import org.apache.logging.log4j.Logger
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableTermRepository
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.TermCollection
import org.sourcegrade.lab.hub.domain.TermRepository
import java.util.UUID

class TermQueries(
    private val logger: Logger,
    private val repository: TermRepository,
) {
    fun term(): TermQuery = TermQuery(logger, repository)
}

class TermQuery(
    private val logger: Logger,
    private val repository: TermRepository,
) {
    suspend fun findAll(
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): TermCollection = repository.findAll(limit.flatten(), orders.flattenList())

    suspend fun findById(dfe: DataFetchingEnvironment, id: UUID): Term? = repository.findById(id, dfe.extractRelations())
    suspend fun exists(id: UUID): Boolean = repository.exists(id)
    suspend fun countAll(): Long = repository.countAll()
}

class TermMutations(
    private val logger: Logger,
    private val repository: MutableTermRepository,
)

class TermMutation(
    private val logger: Logger,
    private val repository: MutableTermRepository,
) {

}
