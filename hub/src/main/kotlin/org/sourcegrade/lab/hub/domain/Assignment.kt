package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.Assignments
import org.sourcegrade.lab.hub.db.Courses
import org.sourcegrade.lab.hub.db.RolePermissionBindings.references
import java.util.UUID

class Assignment(id: EntityID<UUID>) : UUIDEntity(id) {
    val courseId: EntityID<UUID> by Assignments.courseId references Courses.id
    val course: Course by Course referencedOn Assignments.courseId
    val name: String by Assignments.name
    val description: String by Assignments.description
    val submissionDeadline: Instant by Assignments.submissionDeadline
    val submissionGroupCategory: SubmissionGroupCategory by SubmissionGroupCategory referencedOn Assignments.submissionGroupCategory

    companion object : EntityClass<UUID, Assignment>(Assignments)
}
