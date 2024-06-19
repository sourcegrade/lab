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
import java.util.UUID

interface Assignment : DomainEntity {
    val course: Course
    val submissionGroupCategory: SubmissionGroupCategory

    var name: String
    var description: String
    var submissionDeadlineUtc: Instant

    suspend fun setSubmissionGroupCategoryId(id: UUID): Boolean

    data class CreateDto(
        val courseId: UUID,
        val submissionGroupCategoryId: UUID,
        val name: String,
        val description: String,
        val submissionDeadlineUtc: Instant,
    ) : Creates<Assignment>
}
