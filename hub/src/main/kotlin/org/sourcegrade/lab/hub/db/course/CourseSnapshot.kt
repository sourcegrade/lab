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

package org.sourcegrade.lab.hub.db.course

import kotlinx.datetime.Instant
import org.sourcegrade.lab.hub.db.RelationOption
import org.sourcegrade.lab.hub.domain.AssignmentCollection
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategoryCollection
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.User
import java.util.UUID

internal class CourseSnapshot(
    uuidOption: RelationOption<UUID>,
    createdUtcOption: RelationOption<Instant>,
    termOption: RelationOption<Term>,
    submissionGroupCategoriesOption: RelationOption<String>,
    displaynameOption: RelationOption<String>,
) : Course {

    override val uuid: UUID by uuidOption
    override val createdUtc: Instant by createdUtcOption
    override val term: Term by termOption

    override val owner: User
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")
    override val description: String
        get() = TODO("Not yet implemented")

    override fun submissionGroupCategories(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): SubmissionGroupCategoryCollection {
        TODO("Not yet implemented")
    }

    override fun assignments(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): AssignmentCollection {
        TODO("Not yet implemented")
    }

    companion object {
        fun of(user: Course, relations: Set<String>): CourseSnapshot = with(relations) {
            TODO()
//            CourseSnapshot(
//                RelationOption.of(user::uuid),
//                RelationOption.of(user::createdUtc),
//                RelationOption.of(user::email),
//                RelationOption.of(user::username),
//                RelationOption.of(user::displayname),
//            )
        }
    }
}
