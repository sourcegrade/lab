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

package org.sourcegrade.lab.hub.db.course

import kotlinx.datetime.Instant
import org.sourcegrade.lab.hub.db.RelationOption
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.User
import java.util.UUID

//internal class CourseSnapshot(
//    uuidOption: RelationOption<UUID>,
//    createdUtcOption: RelationOption<Instant>,
//    termOption: RelationOption<Term>,
//    submissionGroupCategoriesOption: RelationOption<String>,
//    displaynameOption: RelationOption<String>,
//) : User {
//
//    override val uuid: UUID by uuidOption
//    override val createdUtc: Instant by createdUtcOption
//    override val email: String by emailOption
//    override val username: String by usernameOption
//    override val displayname: String by displaynameOption
//
//    companion object {
//        fun of(user: User, relations: Set<String>): CourseSnapshot = with(relations) {
//            CourseSnapshot(
//                RelationOption.of(user::uuid),
//                RelationOption.of(user::createdUtc),
//                RelationOption.of(user::email),
//                RelationOption.of(user::username),
//                RelationOption.of(user::displayname),
//            )
//        }
//    }
//}
