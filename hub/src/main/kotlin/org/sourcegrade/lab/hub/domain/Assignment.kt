package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.AssignmentTable
import org.sourcegrade.lab.hub.db.CourseTable
import org.sourcegrade.lab.hub.db.RolePermissionTable.references
import java.util.UUID

//data class Assignment(
//    override val id: UUID,
//    val name: String,
//    val description: String,
//    val course: Course,
//    val submissionDeadline: ZonedDateTime,
//    val submissionGroupCategory: SubmissionGroup.Category,
//) : DomainEntity

class Assignment(id: EntityID<UUID>) : UUIDEntity(id) {
    val courseId: EntityID<UUID> by AssignmentTable.courseId references CourseTable.id
    val course: Course by Course referencedOn AssignmentTable.courseId
    val name: String by AssignmentTable.name
    val description: String by AssignmentTable.description

    companion object : EntityClass<UUID, Assignment>(AssignmentTable)
}
