package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object SubmissionGroupMembers : Table("sgl_submission_group_members") {
    val createdUtc = timestamp("createdUtc")
    val endedUtc = timestamp("endedUtc").nullable()
    val userId = reference("user_id", Users)
    val submissionGroupId = reference("submission_group_id", SubmissionGroups)
}
