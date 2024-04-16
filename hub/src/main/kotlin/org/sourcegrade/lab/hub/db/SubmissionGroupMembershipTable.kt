package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.sql.Table

internal object SubmissionGroupMembershipTable : Table("sgl_submission_group_memberships") {
    val userId = reference("user_id", UserTable).uniqueIndex()
    val submissionGroupId = reference("submission_group_id", SubmissionGroupTable)
}
