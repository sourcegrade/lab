package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

interface Course : TermScoped {
    val submissionGroupCategories: SizedIterable<SubmissionGroupCategory>
    val assignments: SizedIterable<Assignment>
    val owner: User

    var name: String
    var description: String
    var ownerId: UUID
}
