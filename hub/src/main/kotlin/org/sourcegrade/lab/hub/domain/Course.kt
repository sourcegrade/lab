package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.Assignments
import org.sourcegrade.lab.hub.db.Courses
import java.util.UUID

//data class Course(
//    override val id: UUID,
//    val name: String,
//    val description: String,
//    val term: Term,
//    val owner: User,
//    val submissionGroupCategories: List<SubmissionGroup.Category>,
//    val assignments: List<Assignment>,
//) : DomainEntity

class Course(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by Courses.name
    val description: String by Courses.description
    val term: String by Courses.term
    val owner: User by User referencedOn Courses.ownerId
    val assignments: SizedIterable<Assignment> by Assignment referrersOn Assignments.courseId

    companion object : EntityClass<UUID, Course>(Courses)
}
