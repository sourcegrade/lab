package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.sql.Table

internal object SubmissionGroupMembers : Table("sgl_submission_group_members") {
    val userId = reference("user_id", Users).uniqueIndex()
    val submissionGroupId = reference("submission_group_id", SubmissionGroups)
}
