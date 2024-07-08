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

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.completeWith
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.domain.ExecutionContext

class SuspendedExecutionContext : ExecutionContext {

    private val executionScopes = mutableListOf<ExecutionScope<*>>()

    private class ExecutionScope<T>(private val statement: suspend () -> T) {
        val future = CompletableDeferred<T>()
        suspend fun execute() {
            future.completeWith(runCatching { statement() })
        }
    }

    override suspend fun <T> execute(statement: suspend () -> T): T {
        val scope = ExecutionScope(statement)
        executionScopes.add(scope)

        return scope.future.await()
    }

    override suspend fun execute() = newSuspendedTransaction {
        executionScopes.forEach { scope ->
            scope.execute()
        }
    }
}
