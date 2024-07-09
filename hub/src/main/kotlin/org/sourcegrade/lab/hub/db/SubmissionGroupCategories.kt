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
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.course.Courses
import org.sourcegrade.lab.hub.db.course.DBCourse
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.MutableSubmissionGroupCategoryRepository
import org.sourcegrade.lab.hub.domain.Relation
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.SizedIterableCollection
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategory
import org.sourcegrade.lab.hub.domain.SubmissionGroupCategoryCollection
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
    override var course: DBCourse by DBCourse referencedOn SubmissionGroupCategories.courseId
    override var name: String by SubmissionGroupCategories.name
    override var minSize: Int by SubmissionGroupCategories.minSize
    override var maxSize: Int by SubmissionGroupCategories.maxSize

    companion object : EntityClass<UUID, DBSubmissionGroupCategory>(SubmissionGroupCategories)
}

internal class SubmissionGroupCategoryRepository(
    private val logger: Logger,
    private val conversionContext: EntityConversionContext<SubmissionGroupCategory, DBSubmissionGroupCategory>,
) : MutableSubmissionGroupCategoryRepository,
    Repository<SubmissionGroupCategory> by UUIDEntityClassRepository(DBSubmissionGroupCategory, conversionContext),
    EntityConversionContext<SubmissionGroupCategory, DBSubmissionGroupCategory> by conversionContext {

    override suspend fun findByName(name: String, relations: List<Relation<SubmissionGroupCategory>>): SubmissionGroupCategory? =
        entityConversion(relations) {
            DBSubmissionGroupCategory.find { SubmissionGroupCategories.name eq name }.firstOrNull().bindNullable()
        }

    override suspend fun findAllByName(
        partialName: String,
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): SubmissionGroupCategoryCollection =
        DBSubmissionGroupCategoryCollection(conversionContext, limit, orders) {
            DBSubmissionGroupCategory.find { SubmissionGroupCategories.name like "%$partialName%" }.bindIterable()
        }

    override suspend fun findAll(
        limit: DomainEntityCollection.Limit?,
        orders: List<DomainEntityCollection.FieldOrdering>,
    ): SubmissionGroupCategoryCollection =
        DBSubmissionGroupCategoryCollection(conversionContext, limit, orders) {
            DBSubmissionGroupCategory.all().bindIterable()
        }

    override suspend fun create(
        item: SubmissionGroupCategory.CreateDto,
        relations: List<Relation<SubmissionGroupCategory>>,
    ): SubmissionGroupCategory = entityConversion(relations) {
        val itemCourse = DBCourse.findByIdNotNull(item.courseUuid)
        DBSubmissionGroupCategory.new {
            course = itemCourse
            name = item.name
            minSize = item.minSize
            maxSize = item.maxSize
        }.also {
            logger.info("Created new SubmissionGroupCategory ${it.id} with data $item")
        }.bind()
    }

    override suspend fun put(
        item: SubmissionGroupCategory.CreateDto,
        relations: List<Relation<SubmissionGroupCategory>>,
    ): MutableRepository.PutResult<SubmissionGroupCategory> {
        TODO("Not yet implemented")
    }
}

internal class DBSubmissionGroupCategoryCollection(
    private val conversionContext: EntityConversionContext<SubmissionGroupCategory, DBSubmissionGroupCategory>,
    private val limit: DomainEntityCollection.Limit?,
    private val orders: List<DomainEntityCollection.FieldOrdering>,
    private val body: ConversionBody<SubmissionGroupCategory, DBSubmissionGroupCategory, SizedIterable<SubmissionGroupCategory>>,
) : SubmissionGroupCategoryCollection,
    DomainEntityCollection<SubmissionGroupCategory, SubmissionGroupCategoryCollection>
    by SizedIterableCollection(SubmissionGroupCategories, conversionContext, limit, orders, body)
