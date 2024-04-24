package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.SubmissionGroup
import org.sourcegrade.lab.hub.domain.Term
import java.util.UUID

internal object SubmissionGroups : UUIDTable("sgl_submission_groups") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255).uniqueIndex()
    val categoryId = reference("category_id", SubmissionGroupCategories)
}

internal class DBSubmissionGroup(id: EntityID<UUID>) : UUIDEntity(id), SubmissionGroup {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by SubmissionGroups.createdUtc
    override val term: Term
        get() = category.course.term
    override val name: String by SubmissionGroups.name
    override val category: DBSubmissionGroupCategory by DBSubmissionGroupCategory referencedOn SubmissionGroups.categoryId
    override val members: SizedIterable<DBUser> by DBUser via SubmissionGroupMemberships

    companion object : EntityClass<UUID, DBSubmissionGroup>(SubmissionGroups)
}
