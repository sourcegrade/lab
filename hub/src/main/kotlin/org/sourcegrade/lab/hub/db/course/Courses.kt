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

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.execution.OptionalInput
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.ConversionBody
import org.sourcegrade.lab.hub.db.DBSubmissionGroupCategory
import org.sourcegrade.lab.hub.db.DBSubmissionGroupCategoryCollection
import org.sourcegrade.lab.hub.db.DBTerm
import org.sourcegrade.lab.hub.db.EntityConversionContext
import org.sourcegrade.lab.hub.db.SubmissionGroupCategories
import org.sourcegrade.lab.hub.db.Terms
import org.sourcegrade.lab.hub.db.UUIDEntityClassRepository
import org.sourcegrade.lab.hub.db.assignment.Assignments
import org.sourcegrade.lab.hub.db.assignment.DBAssignment
import org.sourcegrade.lab.hub.db.findByIdNotNull
import org.sourcegrade.lab.hub.db.user.DBUser
import org.sourcegrade.lab.hub.db.user.Users
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.AssignmentCollection
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.CourseCollection
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableCourse
import org.sourcegrade.lab.hub.domain.MutableCourseRepository
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Relation
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.SizedIterableCollection
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategoryCollection
import java.util.UUID

internal object Courses : UUIDTable("sgl_courses") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val term = reference("term_id", Terms)
    val ownerId = reference("owner_id", Users)
}

@GraphQLIgnore
internal class DBCourse(id: EntityID<UUID>) : UUIDEntity(id), MutableCourse {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Courses.createdUtc
    override var term: DBTerm by DBTerm referencedOn Courses.term
    val submissionGroupCategories: SizedIterable<DBSubmissionGroupCategory> by DBSubmissionGroupCategory referrersOn SubmissionGroupCategories.courseId
    val assignments: SizedIterable<Assignment> by DBAssignment referrersOn Assignments.courseId
    override var owner: DBUser by DBUser referencedOn Courses.ownerId // TODO: Multiple owners

    override var name: String by Courses.name
    override var description: String by Courses.description

    override fun submissionGroupCategories(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): SubmissionGroupCategoryCollection {
        TODO()
//        return DBSubmissionGroupCategoryCollection(conversionContext) {
//            submissionGroupCategories.bindIterable()
//        }
    }

    override fun assignments(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): AssignmentCollection {
        TODO("Not yet implemented")
    }

    companion object : EntityClass<UUID, DBCourse>(Courses)
}

internal class DBCourseRepository(
    private val logger: Logger,
    private val conversionContext: EntityConversionContext<Course, DBCourse>,
) : MutableCourseRepository, Repository<Course> by UUIDEntityClassRepository(DBCourse, conversionContext),
    EntityConversionContext<Course, DBCourse> by conversionContext {
    override suspend fun findAll(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): CourseCollection = DBCourseCollection(conversionContext, limit, orders) { DBCourse.all().bindIterable() }

    override suspend fun findByName(name: String, relations: List<Relation<Course>>): Course? =
        entityConversion(relations) { DBCourse.find { Courses.name eq name }.firstOrNull().bindNullable() }

    override suspend fun findAllByName(partialName: String): CourseCollection =
        DBCourseCollection(conversionContext) { DBCourse.find { Courses.name like "%$partialName%" }.bindIterable() }

    override suspend fun findAllByDescription(partialDescription: String): CourseCollection =
        DBCourseCollection(conversionContext) { DBCourse.find { Courses.description like "%$partialDescription%" }.bindIterable() }

    override suspend fun findAllByOwner(ownerId: UUID): CourseCollection =
        DBCourseCollection(conversionContext) { DBCourse.find { Courses.ownerId eq ownerId }.bindIterable() }

    override suspend fun create(item: Course.CreateDto, relations: List<Relation<Course>>): Course = entityConversion(relations) {
        val itemOwner = DBUser.findByIdNotNull(item.ownerUuid)
        val itemTerm = DBTerm.findByIdNotNull(item.termUuid)
        DBCourse.new {
            name = item.name
            owner = itemOwner
            term = itemTerm
            description = if (item.description is OptionalInput.Defined) {
                requireNotNull(item.description.value) { "description" }
            } else {
                item.name
            }
        }.also {
            logger.info("Created course ${it.uuid}")
        }.bind()
    }

    override suspend fun put(item: Course.CreateDto, relations: List<Relation<Course>>): MutableRepository.PutResult<Course> {
        TODO("Not yet implemented")
    }
}

internal class DBCourseCollection(
    private val conversionContext: EntityConversionContext<Course, DBCourse>,
    private val limit: DomainEntityCollection.Limit? = null,
    private val orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
    private val body: ConversionBody<Course, DBCourse, SizedIterable<Course>>,
) : CourseCollection,
    DomainEntityCollection<Course, CourseCollection> by SizedIterableCollection(Courses, conversionContext, limit, orders, body)
