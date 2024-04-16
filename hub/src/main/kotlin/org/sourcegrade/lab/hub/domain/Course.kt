package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.AssignmentTable
import org.sourcegrade.lab.hub.db.CourseTable
import org.sourcegrade.lab.hub.db.RolePermissionTable.references
import org.sourcegrade.lab.hub.db.UserTable
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
    val name: String by CourseTable.name
    val description: String by CourseTable.description
    val term: String by CourseTable.term
    val owner: User by User referencedOn CourseTable.ownerId
    val assignments: SizedIterable<Assignment> by Assignment referrersOn AssignmentTable.courseId

    companion object : EntityClass<UUID, Course>(CourseTable)
}
