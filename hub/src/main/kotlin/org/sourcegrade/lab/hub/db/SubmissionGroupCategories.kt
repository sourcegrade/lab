package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategory
import java.util.UUID

internal object SubmissionGroupCategories : UUIDTable("sgl_submission_group_categories") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255).uniqueIndex()
    val courseId = reference("course_id", Courses)
    val minSize = integer("min_size")
    val maxSize = integer("max_size")
}

internal class DBSubmissionGroupCategory(id: EntityID<UUID>) : UUIDEntity(id), SubmissionGroupCategory {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by SubmissionGroupCategories.createdUtc
    override val course: DBCourse by DBCourse referencedOn SubmissionGroupCategories.courseId
    override var name: String by SubmissionGroupCategories.name
    override var minSize: Int by SubmissionGroupCategories.minSize
    override var maxSize: Int by SubmissionGroupCategories.maxSize

    companion object : EntityClass<UUID, DBSubmissionGroupCategory>(SubmissionGroupCategories)
}
