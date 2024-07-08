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
import org.sourcegrade.lab.hub.db.EntityConversionContextImpl
import org.sourcegrade.lab.hub.db.UUIDEntityClassRepository
import org.sourcegrade.lab.hub.domain.DomainEntityCollection
import org.sourcegrade.lab.hub.domain.ExecutionContext
import org.sourcegrade.lab.hub.domain.MutableUser
import org.sourcegrade.lab.hub.domain.Relation
import org.sourcegrade.lab.hub.domain.SizedIterableCollection
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserCollection
import org.sourcegrade.lab.hub.domain.repo.MutableRepository
import org.sourcegrade.lab.hub.domain.repo.MutableUserRepository
import org.sourcegrade.lab.hub.domain.repo.Repository
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

    companion object : EntityClass<UUID, DBUser>(Users)
}

private val conversionContext = EntityConversionContextImpl<User, DBUser>(UserSnapshot::of)

internal class DBUserRepository(
    private val logger: Logger,
) : MutableUserRepository, Repository<User> by UUIDEntityClassRepository(DBUser, conversionContext),
    EntityConversionContext<User, DBUser> by conversionContext {

    override suspend fun findByUsername(username: String, context: ExecutionContext, relations: List<Relation<User>>): User? =
        entityConversion(context, relations) {
            DBUser.find { Users.username eq username }.firstOrNull().bindNullable()
        }

    override suspend fun findAllByUsername(partialUsername: String, context: ExecutionContext): UserCollection =
        DBUserCollection(context) { DBUser.find { Users.username like "%$partialUsername%" }.bindIterable() }

    override suspend fun findByEmail(email: String, context: ExecutionContext, relations: List<Relation<User>>): User? =
        entityConversion(context, relations) {
            DBUser.find { Users.email eq email }.firstOrNull().bindNullable()
        }

    override suspend fun findAll(context: ExecutionContext): UserCollection {
        println("findAll start: ${Thread.currentThread().name}")
        val result = DBUserCollection(context) { DBUser.all().bindIterable() }
        println("findAll end: ${Thread.currentThread().name}")
        return result
    }

    override suspend fun create(item: User.CreateDto, context: ExecutionContext): User = entityConversion(context, emptyList()) {
        DBUser.new {
            email = item.email
            username = item.username
            if (item.displayname is OptionalInput.Defined) {
                displayname = requireNotNull(item.displayname.value) { "displayname" }
            }
        }.also {
            logger.info("Created new user ${it.uuid} with data $item")
        }.bind()
    }

    override suspend fun put(item: User.CreateDto, context: ExecutionContext): MutableRepository.PutResult<User> = newSuspendedTransaction {
        val existingUser = findByUsername(item.username)
        if (existingUser == null) {
            MutableRepository.PutResult(create(item), created = true)
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
    private val context: ExecutionContext,
    private val limit: Pair<Int, Long>? = null,
    private val orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
    private val body: ConversionBody<User, DBUser, SizedIterable<User>>,
) : UserCollection,
    DomainEntityCollection<User, UserCollection>
    by SizedIterableCollection(Users, conversionContext, ::DBUserCollection, context, limit, orders, body)
