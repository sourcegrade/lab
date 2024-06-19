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

package org.sourcegrade.lab.hub.domain

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

interface User : DomainEntity {
    var email: String
    var username: String
    var displayname: String

    // TODO: Split into UserActions
    // TODO: Get from UserMembershipRepository
    @GraphQLIgnore
    suspend fun courseMemberships(
        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<UserMembership<Course>>

    suspend fun courseMemberships(
        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
        term: String = Term.Matcher.Current.toString(),
        now: Instant = Clock.System.now(),
    ): SizedIterable<UserMembership<Course>> = courseMemberships(status, Term.Matcher.fromString(term), now)

    @GraphQLIgnore
    suspend fun submissionGroupMemberships(
        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<UserMembership<SubmissionGroup>>

    suspend fun submissionGroupMemberships(
        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
        term: String = Term.Matcher.Current.toString(),
        now: Instant = Clock.System.now(),
    ): SizedIterable<UserMembership<SubmissionGroup>> = submissionGroupMemberships(status, Term.Matcher.fromString(term), now)

//    suspend fun assignmentParticipations(
//        status: AssignmentParticipation.Status = AssignmentParticipation.Status.OPEN,
//        term: Term.Matcher = Term.Matcher.Current,
//        now: Instant = Clock.System.now(),
//    ): SizedIterable<AssignmentParticipation>

    @GraphQLIgnore
    suspend fun assignments(
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<Assignment>

    suspend fun assignments(
        term: String = Term.Matcher.Current.toString(),
        now: Instant = Clock.System.now(),
    ): SizedIterable<Assignment> = assignments(Term.Matcher.fromString(term), now)

    @GraphQLIgnore
    suspend fun submissions(
        status: Submission.SubmissionStatus = Submission.SubmissionStatus.ALL,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<Submission>

    suspend fun submissions(
        status: Submission.SubmissionStatus = Submission.SubmissionStatus.ALL,
        term: String = Term.Matcher.Current.toString(),
        now: Instant = Clock.System.now(),
    ): SizedIterable<Submission> = submissions(status, Term.Matcher.fromString(term), now)

    data class CreateDto(
        val email: String,
        val username: String,
        val displayname: String = username,
    ) : Creates<User>

    data class Snapshot(
        val uuid: UUID,
        val email: String,
        val username: String,
        val displayname: String,
    )

    fun toSnapshot(): Snapshot = Snapshot(uuid, email, username, displayname)
}
