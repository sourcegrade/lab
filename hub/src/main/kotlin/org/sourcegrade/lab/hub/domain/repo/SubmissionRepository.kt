package org.sourcegrade.lab.hub.domain.repo

import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.Submission
import java.util.UUID

interface SubmissionRepository : Repository<Submission> {
    suspend fun findByAssignment(assignmentId: UUID): SizedIterable<Submission>
    suspend fun findByUserAndAssignment(userId: UUID, assignmentId: UUID): SizedIterable<Submission>
}

interface MutableSubmissionRepository : SubmissionRepository, MutableRepository<Submission, Submission.CreateDto>
