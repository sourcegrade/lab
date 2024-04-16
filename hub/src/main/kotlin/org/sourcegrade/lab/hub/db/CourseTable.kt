package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.AssignmentEntity.Companion.referrersOn
import org.sourcegrade.lab.hub.db.RolePermissionTable.references
import java.util.UUID

internal object CourseTable : UUIDTable("sgl_courses") {
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val term = varchar("term", 255)
    val ownerId = reference("owner_id", UserTable.id)
}

internal class CourseEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    val name by CourseTable.name
    val description by CourseTable.description
    val term by CourseTable.term

    val owner: EntityID<UUID> by CourseTable.ownerId references UserTable.id
    val assignments: SizedIterable<AssignmentEntity> by AssignmentEntity referrersOn AssignmentTable.courseId
}
