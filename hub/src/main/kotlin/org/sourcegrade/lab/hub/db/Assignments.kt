package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.repo.MutableAssignmentRepository
import java.util.UUID

internal object Assignments : UUIDTable("sgl_assignments") {
    val createdUtc = timestamp("createdUtc")
    val courseId = reference("course_id", Courses)
    val submissionGroupCategory = reference("submission_group_category", SubmissionGroupCategories.id)

    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 16 * 1024)
    val submissionDeadline = timestamp("submissionDeadline")
}

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
    override suspend fun findByCourse(courseId: UUID): SizedIterable<Assignment> =
        newSuspendedTransaction { DBAssignment.find { Assignments.courseId eq courseId } }

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
