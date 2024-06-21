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

package org.sourcegrade.lab.hub.db.assignment

import kotlinx.datetime.Instant
import org.sourcegrade.lab.hub.db.RelationOption
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategory
import java.util.UUID

internal class AssignmentSnapshot(
    uuidOption: RelationOption<UUID>,
    createdUtcOption: RelationOption<Instant>,
    courseOption: RelationOption<Course>,
    submissionGroupCategoryOption: RelationOption<SubmissionGroupCategory>,
    nameOption: RelationOption<String>,
    descriptionOption: RelationOption<String>,
    submissionDeadlineUtcOption: RelationOption<Instant>,
) : Assignment {
    override val uuid: UUID by uuidOption
    override val createdUtc: Instant by createdUtcOption
    override val course: Course by courseOption
    override val submissionGroupCategory: SubmissionGroupCategory by submissionGroupCategoryOption
    override val name: String by nameOption
    override val description: String by descriptionOption
    override val submissionDeadlineUtc: Instant by submissionDeadlineUtcOption

    companion object {
        fun of(assignment: Assignment, relations: Set<String>): AssignmentSnapshot = with(relations) {
            AssignmentSnapshot(
                RelationOption.of(assignment::uuid),
                RelationOption.of(assignment::createdUtc),
                RelationOption.of(assignment::course),
                RelationOption.of(assignment::submissionGroupCategory),
                RelationOption.of(assignment::name),
                RelationOption.of(assignment::description),
                RelationOption.of(assignment::submissionDeadlineUtc),
            )
        }
    }
}
