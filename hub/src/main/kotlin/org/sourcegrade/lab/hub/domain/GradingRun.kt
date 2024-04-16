package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.GradingRunTable
import java.util.UUID

//data class GradingRun(
//    override val id: UUID,
//    val maxPoints: Int,
//    val minPoints: Int,
//    val rubric: ByteArray,
//) : DomainEntity

class GradingRun(id: EntityID<UUID>) : UUIDEntity(id) {
    val maxPoints: Int by GradingRunTable.maxPoints
    val minPoints: Int by GradingRunTable.minPoints
}
