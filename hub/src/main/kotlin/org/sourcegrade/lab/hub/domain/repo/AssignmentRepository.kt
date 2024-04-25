package org.sourcegrade.lab.hub.domain.repo

import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Repository
import java.util.UUID

interface AssignmentRepository : Repository<Assignment> {
    suspend fun findByCourse(courseId: UUID): SizedIterable<Assignment>
}

interface MutableAssignmentRepository : AssignmentRepository, MutableRepository<Assignment, Assignment.CreateDto>
