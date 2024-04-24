package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

internal object SubmissionGroupMemberships : Table("sgl_submission_group_membership") {
    val createdUtc = timestamp("createdUtc")
    val startUtc = timestamp("startUtc")
    val endUtc = timestamp("endUtc").nullable()
    val userId = reference("user_id", Users)
    val submissionGroupId = reference("submission_group_id", SubmissionGroups)
}

internal class DBSubmissionGroupMembership(id: EntityID<UUID>) : UUIDEntity(id), SubmissionGroupMembership {
    override val uuid: UUID = id.value
    override val createdUtc by SubmissionGroupMemberships.createdUtc
    override val startUtc by SubmissionGroupMemberships.startUtc
    override val endUtc by SubmissionGroupMemberships.endUtc
    override val user: DBUser by DBUser referencedOn SubmissionGroupMemberships.userId
    override val submissionGroup: DBSubmissionGroup by DBSubmissionGroup referencedOn SubmissionGroupMemberships.submissionGroupId
}
