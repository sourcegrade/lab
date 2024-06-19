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

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.Users.clientDefault
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.Term
import java.util.UUID

internal object Courses : UUIDTable("sgl_courses") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val term = reference("term_id", Terms)
    val ownerId = reference("owner_id", Users)
}

@GraphQLIgnore
internal class DBCourse(id: EntityID<UUID>) : UUIDEntity(id), Course {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Courses.createdUtc
    override val term: Term by DBTerm referencedOn Courses.term
    override val submissionGroupCategories: SizedIterable<DBSubmissionGroupCategory> by DBSubmissionGroupCategory referrersOn SubmissionGroupCategories.courseId
    override val assignments: SizedIterable<Assignment> by DBAssignment referrersOn Assignments.courseId
    override val owner: DBUser by DBUser referencedOn Courses.ownerId // TODO: Multiple owners

    override var name: String by Courses.name
    override var description: String by Courses.description

    companion object : EntityClass<UUID, DBCourse>(Courses)
}
