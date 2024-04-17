package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object Submissions : UUIDTable("sgl_submissions") {
    val assignmentId = reference("assignment_id", Assignments)
    val submitterId = reference("submitter_id", Users)
    val groupId = reference("group_id", SubmissionGroups)
    val uploaded = timestamp("uploaded")
}
