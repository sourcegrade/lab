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

import kotlinx.datetime.Instant
import org.sourcegrade.lab.hub.db.RelationOption
import org.sourcegrade.lab.hub.domain.User
import java.util.UUID

internal class UserSnapshot(
    uuidOption: RelationOption<UUID>,
    createdUtcOption: RelationOption<Instant>,
    emailOption: RelationOption<String>,
    usernameOption: RelationOption<String>,
    displaynameOption: RelationOption<String>,
) : User {

    override val uuid: UUID by uuidOption
    override val createdUtc: Instant by createdUtcOption
    override val email: String by emailOption
    override val username: String by usernameOption
    override val displayname: String by displaynameOption

    // TODO: Split into UserActions
    // TODO: Get from UserMembershipRepository
//    suspend fun assignments(
//        term: String = Term.Matcher.Current.toString(),
//        now: Instant = Clock.System.now(),
//    ): AssignmentCollection = TODO()
//
//    suspend fun courseMemberships(
//        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
//        term: String = Term.Matcher.Current.toString(),
//        now: Instant = Clock.System.now(),
//    ): List<UserMembership<Course>> = TODO()
//
//    suspend fun submissionGroupMemberships(
//        status: UserMembership.UserMembershipStatus = UserMembership.UserMembershipStatus.CURRENT,
//        term: String = Term.Matcher.Current.toString(),
//        now: Instant = Clock.System.now(),
//    ): List<UserMembership<SubmissionGroup>> = TODO()
//
//    suspend fun submissions(
//        status: Submission.SubmissionStatus = Submission.SubmissionStatus.ALL,
//        term: String = Term.Matcher.Current.toString(),
//        now: Instant = Clock.System.now(),
//    ): SizedIterable<Submission> = submissions(status, Term.Matcher.fromString(term), now)

    companion object {
        fun of(user: User, relations: Set<String>): UserSnapshot = with(relations) {
            UserSnapshot(
                RelationOption.of(user::uuid),
                RelationOption.of(user::createdUtc),
                RelationOption.of(user::email),
                RelationOption.of(user::username),
                RelationOption.of(user::displayname),
            )
        }
    }
}
