package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.companionObjectInstance

internal suspend inline fun <ID : Comparable<ID>, reified E : Entity<ID>> KMutableProperty0<E>.mutateReference(id: ID): Boolean {
    @Suppress("UNCHECKED_CAST")
    val entityClass: EntityClass<ID, E> =
        checkNotNull(E::class.companionObjectInstance as? EntityClass<ID, E>) {
            "Companion EntityClass for ${E::class} not found"
        }

    return newSuspendedTransaction {
        val entity = entityClass.findById(id)
        if (entity == null) {
            false
        } else {
            set(entity)
            true
        }
    }
}

internal inline fun <ID : Comparable<ID>, reified E : Entity<ID>> EntityClass<ID, E>.findByIdNotNull(id: ID): E =
    findById(id) ?: error("${E::class.simpleName} $id not found")
