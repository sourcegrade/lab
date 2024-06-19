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

package org.sourcegrade.lab.hub.db

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.Users.clientDefault
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategory
import java.util.UUID

internal object SubmissionGroupCategories : UUIDTable("sgl_submission_group_categories") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val name = varchar("name", 255).uniqueIndex()
    val courseId = reference("course_id", Courses)
    val minSize = integer("min_size")
    val maxSize = integer("max_size")
}

@GraphQLIgnore
internal class DBSubmissionGroupCategory(id: EntityID<UUID>) : UUIDEntity(id), SubmissionGroupCategory {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by SubmissionGroupCategories.createdUtc
    override val course: DBCourse by DBCourse referencedOn SubmissionGroupCategories.courseId
    override var name: String by SubmissionGroupCategories.name
    override var minSize: Int by SubmissionGroupCategories.minSize
    override var maxSize: Int by SubmissionGroupCategories.maxSize

    companion object : EntityClass<UUID, DBSubmissionGroupCategory>(SubmissionGroupCategories)
}
