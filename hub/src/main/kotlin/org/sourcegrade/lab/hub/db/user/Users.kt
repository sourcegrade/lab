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
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.db.ConversionBody
import org.sourcegrade.lab.hub.db.EntityConversionContext
import org.sourcegrade.lab.hub.db.UUIDEntityClassRepository
import org.sourcegrade.lab.hub.domain.AssignmentCollection
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.MutableUser
import org.sourcegrade.lab.hub.domain.MutableUserRepository
import org.sourcegrade.lab.hub.domain.Relation
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.SizedIterableCollection
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserCollection
import java.util.UUID

internal object Users : UUIDTable("sgl_users") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val displayname = varchar("displayname", 255)
}

@GraphQLIgnore
internal class DBUser(id: EntityID<UUID>) : UUIDEntity(id), MutableUser {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Users.createdUtc
    override var email: String by Users.email
    override var username: String by Users.username
    override var displayname: String by Users.displayname

    override suspend fun assignments(
        term: OptionalInput<String>,
        now: OptionalInput<Instant>,
        limit: OptionalInput<DomainEntityCollection.Limit>,
        orders: OptionalInput<List<DomainEntityCollection.FieldOrdering>>,
    ): AssignmentCollection {
        TODO("Not yet implemented")
    }

    companion object : EntityClass<UUID, DBUser>(Users)
}

internal class DBUserRepository(
    private val logger: Logger,
    private val conversionContext: EntityConversionContext<User, DBUser>,
) : MutableUserRepository, Repository<User> by UUIDEntityClassRepository(DBUser, conversionContext),
    EntityConversionContext<User, DBUser> by conversionContext {

    override suspend fun findByUsername(username: String, relations: List<Relation<User>>): User? =
        entityConversion(relations) { DBUser.find { Users.username eq username }.firstOrNull().bindNullable() }

    override suspend fun findByEmail(email: String, relations: List<Relation<User>>): User? =
        entityConversion { DBUser.find { Users.email eq email }.firstOrNull().bindNullable() }

    override suspend fun findAllByUsername(partialUsername: String): UserCollection =
        DBUserCollection(conversionContext) { DBUser.find { Users.username like "%$partialUsername%" }.bindIterable() }

    override suspend fun findAll(limit: DomainEntityCollection.Limit?, orders: List<DomainEntityCollection.FieldOrdering>): UserCollection =
        DBUserCollection(conversionContext, limit, orders) { DBUser.all().bindIterable() }

    override suspend fun create(item: User.CreateUserDto, relations: List<Relation<User>>): User = entityConversion(relations) {
        DBUser.new {
            email = item.email
            username = item.username
            displayname = if (item.displayname is OptionalInput.Defined) {
                requireNotNull(item.displayname.value) { "displayname" }
            } else {
                item.username
            }
        }.also {
            logger.info("Created new user ${it.uuid} with data $item")
        }.bind()
    }

    override suspend fun put(
        item: User.CreateUserDto,
        relations: List<Relation<User>>,
    ): MutableRepository.PutResult<User> = newSuspendedTransaction {
        val existingUser = findByUsername(item.username, relations)
        if (existingUser == null) {
            MutableRepository.PutResult(create(item, relations), created = true)
        } else {
            logger.info(
                "Loaded existing user ${existingUser.username} (${existingUser.uuid}) with" +
                    "display name ${existingUser.displayname} and email ${existingUser.email}",
            )
            MutableRepository.PutResult(existingUser, created = false)
        }
    }
}

internal class DBUserCollection(
    private val conversionContext: EntityConversionContext<User, DBUser>,
    private val limit: DomainEntityCollection.Limit? = null,
    private val orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
    private val body: ConversionBody<User, DBUser, SizedIterable<User>>,
) : UserCollection,
    DomainEntityCollection<User, UserCollection> by SizedIterableCollection(Users, conversionContext, limit, orders, body)
