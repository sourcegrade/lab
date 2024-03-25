package org.sourcegrade.lab.hub.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

enum class SemesterType {
    WS,
    SS,
}

object Courses : Models("courses") {
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val semesterType = enumerationByName("semesterType", 2, SemesterType::class)
    val semesterStartYear = integer("semesterStartYear")
}

class Course(id: EntityID<String>) : Model<CourseDTO>(id) {
    companion object : EntityClass<String, Course>(Courses)

    var name by Courses.name
    var description by Courses.description
    var semesterType by Courses.semesterType
    var semesterStartYear by Courses.semesterStartYear
    var members by User via CourseMembers

    override fun toDTO(): CourseDTO {
        return CourseDTO(
            this.id.value,
            this.name,
            this.description,
            this.semesterType,
            this.semesterStartYear,
        )
    }
}

@Serializable
class CourseDTO(
    val id: String,
    val name: String,
    val description: String,
    val semesterType: SemesterType,
    val semesterStartYear: Int,
) {
    suspend fun members(): List<UserDTO> {
        return newSuspendedTransaction {
            Course.findById(this@CourseDTO.id)?.members?.map { it.toDTO() }
                ?: throw IllegalArgumentException("No Course with id $id found")
        }
    }
}

fun ResultRow.toCourseDTO(): CourseDTO {
    return CourseDTO(
        this[Courses.id].value,
        this[Courses.name],
        this[Courses.description],
        this[Courses.semesterType],
        this[Courses.semesterStartYear],
    )
}
