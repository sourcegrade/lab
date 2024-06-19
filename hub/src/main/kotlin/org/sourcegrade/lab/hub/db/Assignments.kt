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
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.db.Users.clientDefault
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.repo.MutableAssignmentRepository
import java.util.UUID

internal object Assignments : UUIDTable("sgl_assignments") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val courseId = reference("course_id", Courses)
    val submissionGroupCategory = reference("submission_group_category", SubmissionGroupCategories.id)

    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 16 * 1024)
    val submissionDeadline = timestamp("submissionDeadline")
}

@GraphQLIgnore
internal class DBAssignment(id: EntityID<UUID>) : UUIDEntity(id), Assignment {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Assignments.createdUtc

    override var submissionGroupCategory: DBSubmissionGroupCategory by DBSubmissionGroupCategory referencedOn Assignments.submissionGroupCategory
    override var course: DBCourse by DBCourse referencedOn Assignments.courseId

    override var name: String by Assignments.name
    override var description: String by Assignments.description
    override var submissionDeadlineUtc: Instant by Assignments.submissionDeadline

    override suspend fun setSubmissionGroupCategoryId(id: UUID): Boolean = ::course.mutateReference(id)

    companion object : EntityClass<UUID, DBAssignment>(Assignments)
}

internal class DBAssignmentRepository : MutableAssignmentRepository, Repository<Assignment> by UUIDEntityClassRepository(DBAssignment) {
    override suspend fun findByCourse(courseId: UUID): SizedIterable<Assignment> = newSuspendedTransaction {
        DBAssignment.find { Assignments.courseId eq courseId }
    }

    override suspend fun create(item: Assignment.CreateDto): Assignment = newSuspendedTransaction {
        DBAssignment.new {
            course = DBCourse.findByIdNotNull(item.courseId)
            submissionGroupCategory = DBSubmissionGroupCategory.findByIdNotNull(item.submissionGroupCategoryId)
            name = item.name
            description = item.description
            submissionDeadlineUtc = item.submissionDeadlineUtc
        }
    }

    override suspend fun put(item: Assignment.CreateDto): MutableRepository.PutResult<Assignment> {
        TODO("Not yet implemented")
    }
}
