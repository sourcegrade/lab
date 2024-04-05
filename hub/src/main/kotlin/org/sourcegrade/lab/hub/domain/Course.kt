package org.sourcegrade.lab.hub.domain

import org.sourcegrade.lab.hub.db.DomainEntity
import java.util.UUID

data class Course(
    override val id: UUID,
    val name: String,
    val description: String,
    val semester: Semester,
    val assignments: List<Assignment>,
) : DomainEntity {

}
