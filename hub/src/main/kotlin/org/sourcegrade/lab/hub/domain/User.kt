package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable

interface User : DomainEntity {
    var username: String
    var displayname: String
    var email: String

    // TODO: Split into UserActions
    // TODO: Get from UserMembershipRepository
    suspend fun courseMemberships(
        status: UserMembership.Status = UserMembership.Status.CURRENT,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<UserMembership<Course>>

    suspend fun submissionGroupMemberships(
        status: UserMembership.Status = UserMembership.Status.CURRENT,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<UserMembership<SubmissionGroup>>

//    suspend fun assignmentParticipations(
//        status: AssignmentParticipation.Status = AssignmentParticipation.Status.OPEN,
//        term: Term.Matcher = Term.Matcher.Current,
//        now: Instant = Clock.System.now(),
//    ): SizedIterable<AssignmentParticipation>

    suspend fun assignments(
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<Assignment>

    suspend fun submissions(
        status: Submission.Status = Submission.Status.ALL,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<Submission>
}
