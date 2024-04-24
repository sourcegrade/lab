package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.Assignments
import org.sourcegrade.lab.hub.db.Courses
import org.sourcegrade.lab.hub.db.SubmissionGroupCategories
import java.util.UUID

class Course(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by Courses.name
    val description: String by Courses.description
    val term: String by Courses.term
    val owner: User by User referencedOn Courses.ownerId // TODO: Multiple owners
    val submissionGroupCategories: SizedIterable<SubmissionGroupCategory> by SubmissionGroupCategory referrersOn SubmissionGroupCategories.courseId
    val assignments: SizedIterable<Assignment> by Assignment referrersOn Assignments.courseId

    companion object : EntityClass<UUID, Course>(Courses)
}
