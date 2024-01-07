package org.sourcegrade.yougrade.hub.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow

object CourseMembers : Models("course_members") {
    val course = reference("course_id", Courses)
    val user = reference("user_id", Users)
    override val primaryKey = PrimaryKey(course, user, name = "pk_${tableName}_course_user")
}

@Serializable
class CourseMemberDTO(
    val id: String,
    val course: String,
    val user: String,
)

class CourseMember(id: EntityID<String>) : Model<CourseMemberDTO>(id) {
    companion object : EntityClass<String, CourseMember>(CourseMembers)

    var course by Course referencedOn CourseMembers.course
    var user by User referencedOn CourseMembers.user

    override fun toDTO(): CourseMemberDTO {
        return CourseMemberDTO(
            this.id.value,
            this.course.id.value,
            this.user.id.value,
        )
    }
}

fun ResultRow.toCourseMemberDTO(): CourseMemberDTO {
    return CourseMemberDTO(
        this[CourseMembers.id].value,
        this[CourseMembers.course].value,
        this[CourseMembers.user].value,
    )
}
