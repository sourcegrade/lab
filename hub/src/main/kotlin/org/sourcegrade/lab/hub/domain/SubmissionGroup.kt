package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable

interface SubmissionGroup : TermScoped {
    val name: String
    val category: SubmissionGroupCategory
    val members: SizedIterable<User>
}
