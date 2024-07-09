/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.lab.hub.db.user

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.db.CourseMemberships
import org.sourcegrade.lab.hub.db.DBCourseMembership
import org.sourcegrade.lab.hub.db.DBSubmission
import org.sourcegrade.lab.hub.db.DBSubmissionGroupMembership
import org.sourcegrade.lab.hub.db.SubmissionGroupMemberships
import org.sourcegrade.lab.hub.db.SubmissionGroups
import org.sourcegrade.lab.hub.db.Submissions
import org.sourcegrade.lab.hub.db.Terms
import org.sourcegrade.lab.hub.db.course.Courses
import org.sourcegrade.lab.hub.domain.AssignmentCollection
import org.sourcegrade.lab.hub.domain.AssignmentRepository
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.Submission
import org.sourcegrade.lab.hub.domain.SubmissionGroup
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserMembership
import org.sourcegrade.lab.hub.domain.termPredicate

internal fun SqlExpressionBuilder.membershipStatusPredicate(status: UserMembership.UserMembershipStatus, now: Instant): Op<Boolean> =
    when (status) {
        UserMembership.UserMembershipStatus.ALL -> Op.TRUE
        UserMembership.UserMembershipStatus.FUTURE -> CourseMemberships.startUtc greater now
        UserMembership.UserMembershipStatus.PAST -> CourseMemberships.endUtc less now
        UserMembership.UserMembershipStatus.CURRENT -> CourseMemberships.endUtc.isNull()
    }

suspend fun User.assignments(
    assignmentRepository: AssignmentRepository,
    term: Term.Matcher = Term.Matcher.Current,
    now: Instant = Clock.System.now(),
    limit: DomainEntityCollection.Limit? = null,
    orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
): AssignmentCollection = assignmentRepository.findAllByUser(uuid, term, now, limit, orders)

suspend fun User.courseMemberships(
    status: UserMembership.UserMembershipStatus,
    term: Term.Matcher,
    now: Instant,
): SizedIterable<UserMembership<Course>> = newSuspendedTransaction {
    CourseMemberships.innerJoin(Courses).innerJoin(Terms).selectAll()
        .where {
            (CourseMemberships.userId eq uuid)
                .and(termPredicate(term, now))
                .and(membershipStatusPredicate(status, now))
        }
        .mapLazy { DBCourseMembership.wrapRow(it) }
}

suspend fun User.submissionGroupMemberships(
    status: UserMembership.UserMembershipStatus,
    term: Term.Matcher,
    now: Instant,
): SizedIterable<UserMembership<SubmissionGroup>> = newSuspendedTransaction {
    SubmissionGroupMemberships.innerJoin(SubmissionGroups).innerJoin(Terms).selectAll()
        .where {
            (CourseMemberships.userId eq uuid)
                .and(termPredicate(term, now))
                .and(membershipStatusPredicate(status, now))
        }
        .mapLazy { DBSubmissionGroupMembership.wrapRow(it) }
}

suspend fun User.submissions(
    status: Submission.SubmissionStatus, // TODO: Use parameter
    term: Term.Matcher,
    now: Instant,
): SizedIterable<Submission> = newSuspendedTransaction {
    Submissions.innerJoin(SubmissionGroupMemberships) { (SubmissionGroupMemberships.userId eq uuid) and termPredicate(term, now) }
        .selectAll()
        .mapLazy { DBSubmission.wrapRow(it) }
}

//    suspend fun assignmentParticipations(
//        status: AssignmentParticipation.Status = AssignmentParticipation.Status.OPEN,
//        term: Term.Matcher = Term.Matcher.Current,
//        now: Instant = Clock.System.now(),
//    ): SizedIterable<AssignmentParticipation>
