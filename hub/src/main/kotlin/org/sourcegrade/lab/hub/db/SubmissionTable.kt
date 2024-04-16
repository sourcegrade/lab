package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object SubmissionTable : UUIDTable("sgl_submissions") {
    val assignmentId = reference("assignment_id", AssignmentTable)
    val submitterId = reference("submitter_id", UserTable)
    val groupId = reference("group_id", SubmissionGroupTable)
}
