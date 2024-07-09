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

package org.sourcegrade.lab.hub.db.assignment

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.selectAll
import org.sourcegrade.lab.hub.db.ConversionBody
import org.sourcegrade.lab.hub.db.CourseMemberships
import org.sourcegrade.lab.hub.db.DBSubmissionGroupCategory
import org.sourcegrade.lab.hub.db.EntityConversionContext
import org.sourcegrade.lab.hub.db.SubmissionGroupCategories
import org.sourcegrade.lab.hub.db.Terms
import org.sourcegrade.lab.hub.db.UUIDEntityClassRepository
import org.sourcegrade.lab.hub.db.course.Courses
import org.sourcegrade.lab.hub.db.course.DBCourse
import org.sourcegrade.lab.hub.db.findByIdNotNull
import org.sourcegrade.lab.hub.db.user.membershipStatusPredicate
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.AssignmentCollection
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableAssignment
import org.sourcegrade.lab.hub.domain.MutableAssignmentRepository
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Relation
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.SizedIterableCollection
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.UserMembership
import org.sourcegrade.lab.hub.domain.termPredicate
import java.util.UUID

internal object Assignments : UUIDTable("sgl_assignments") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val courseId = reference("course_id", Courses)
    val submissionGroupCategory = reference("submission_group_category", SubmissionGroupCategories.id)

    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 16 * 1024)
    val submissionDeadline = timestamp("submissionDeadline")
}

@GraphQLIgnore
internal class DBAssignment(id: EntityID<UUID>) : UUIDEntity(id), MutableAssignment {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Assignments.createdUtc

    override var submissionGroupCategory: DBSubmissionGroupCategory by DBSubmissionGroupCategory referencedOn Assignments.submissionGroupCategory
    override var course: DBCourse by DBCourse referencedOn Assignments.courseId

    override var name: String by Assignments.name
    override var description: String by Assignments.description
    override var submissionDeadlineUtc: Instant by Assignments.submissionDeadline

//    override suspend fun setSubmissionGroupCategoryId(id: UUID): Boolean = ::course.mutateReference(id)

    companion object : EntityClass<UUID, DBAssignment>(Assignments)
}

internal class DBAssignmentRepository(
    private val logger: Logger,
    private val conversionContext: EntityConversionContext<Assignment, DBAssignment>,
) : MutableAssignmentRepository, Repository<Assignment> by UUIDEntityClassRepository(DBAssignment, conversionContext),
    EntityConversionContext<Assignment, DBAssignment> by conversionContext {
    override suspend fun findAllByCourse(
        courseId: UUID,
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): AssignmentCollection =
        DBAssignmentCollection(conversionContext, limit, orders) { DBAssignment.find { Assignments.courseId eq courseId }.bindIterable() }

    override suspend fun findAllByName(
        partialName: String,
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): AssignmentCollection =
        DBAssignmentCollection(conversionContext, limit, orders) {
            DBAssignment.find { Assignments.name like "%$partialName%" }.bindIterable()
        }

    override suspend fun findAllByUser(
        userId: UUID,
        term: Term.Matcher,
        now: Instant,
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): AssignmentCollection =
        DBAssignmentCollection(conversionContext, limit, orders) {
            Assignments.innerJoin(Courses).innerJoin(Terms).innerJoin(CourseMemberships).selectAll()
                .where {
                    (CourseMemberships.userId eq userId)
                        .and(termPredicate(term, now))
                        .and(membershipStatusPredicate(UserMembership.UserMembershipStatus.CURRENT, now))
                }
                .mapLazy { DBAssignment.wrapRow(it) }
                .orderBy(Assignments.submissionDeadline to SortOrder.DESC)
                .bindIterable()
        }

    override suspend fun findAll(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): AssignmentCollection =
        DBAssignmentCollection(conversionContext, limit, orders) { DBAssignment.all().bindIterable() }

    override suspend fun create(item: Assignment.CreateAssignmentDto, relations: List<Relation<Assignment>>): Assignment =
        entityConversion(relations) {
            DBAssignment.new {
                course = DBCourse.findByIdNotNull(item.courseId)
                submissionGroupCategory = DBSubmissionGroupCategory.findByIdNotNull(item.submissionGroupCategoryId)
                name = item.name
                description = item.description
                submissionDeadlineUtc = item.submissionDeadlineUtc
            }.also {
                logger.info("Created new Assignment ${it.uuid} with data $item")
            }.bind()
        }

    override suspend fun put(
        item: Assignment.CreateAssignmentDto,
        relations: List<Relation<Assignment>>,
    ): MutableRepository.PutResult<Assignment> {
        val existingAssignment =
            DBAssignment.find { (Assignments.courseId eq item.courseId) and (Assignments.name eq item.name) }.firstOrNull()
        return if (existingAssignment == null) {
            MutableRepository.PutResult(create(item, relations), created = true)
        } else {
            logger.info("Loaded existing assignment ${existingAssignment.name} (${existingAssignment.uuid})")
            MutableRepository.PutResult(existingAssignment, created = false)
        }
    }
}

internal class DBAssignmentCollection(
    private val conversionContext: EntityConversionContext<Assignment, DBAssignment>,
    private val limit: DomainEntityCollection.Limit?,
    private val orders: List<DomainEntityCollection.FieldOrdering>,
    private val body: ConversionBody<Assignment, DBAssignment, SizedIterable<Assignment>>,
) : AssignmentCollection,
    DomainEntityCollection<Assignment, AssignmentCollection>
    by SizedIterableCollection(Assignments, conversionContext, limit, orders, body)
