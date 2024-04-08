package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class SubmissionGroup(
    override val id: UUID,
    val users: List<User>,
    val category: Category,
) : DomainEntity {

    data class Category(
        override val id: UUID,
        val name: String,
        val minSize: Int,
        val maxSize: Int,
    ) : DomainEntity
}
