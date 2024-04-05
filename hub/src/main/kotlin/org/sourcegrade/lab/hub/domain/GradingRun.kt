package org.sourcegrade.lab.hub.domain

import java.util.UUID

data class GradingRun(
    override val id: UUID,
    val maxPoints: Int,
    val minPoints: Int,
    val rubric: ByteArray,
) : DomainEntity {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as GradingRun
        if (id != other.id) return false
        if (maxPoints != other.maxPoints) return false
        if (minPoints != other.minPoints) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + maxPoints
        result = 31 * result + minPoints
        return result
    }
}
