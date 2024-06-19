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

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

interface Submission : DomainEntity {
    val assignment: Assignment
    val submitter: User
    val group: SubmissionGroup
    val uploaded: Instant
    val gradingRuns: SizedIterable<GradingRun>
    val lastGradingRun: GradingRun?

    enum class SubmissionStatus {
        ALL,
        PENDING_GRADE,
        GRADED,
    }

    data class CreateDto(
        val assignmentId: UUID,
        val bytes: ByteArray,
    ) : Creates<Submission>
}
