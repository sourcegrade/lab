package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun <ID : Comparable<ID>> IdTable<ID>.referenceMutator(column: Column<EntityID<ID>>): ReferenceMutator<ID> = ReferenceMutator(this, column)

class ReferenceMutator<ID : Comparable<ID>>(
    private val table: IdTable<ID>,
    private val column: Column<EntityID<ID>>,
) {
    operator fun getValue(self: Entity<ID>, property: KProperty<*>): ID =
        with(self) { column.getValue(self, property).value }

    operator fun setValue(self: Entity<ID>, property: KProperty<*>, value: ID) =
        with(self) { column.setValue(self, property, EntityID(value, table)) }
}
