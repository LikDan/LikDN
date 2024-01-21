package com.likdn.utils.sql.array

import org.jetbrains.exposed.sql.*

inline fun <reified T : Any> Column<T>.array(): Column<ArrayList<T>> =
    this.table.replaceColumn(this, Column(this.table, this.name, ArrayColumnType(this)))

infix fun <T, S> ExpressionWithColumnType<T>.any(t: S): Op<Boolean> =
    t?.let { AnyOp(this, QueryParameter(it, columnType)) } ?: IsNullOp(this)

infix fun <T, S> ExpressionWithColumnType<T>.contains(array: Array<in S>): Op<Boolean> =
    ContainsOp(this, QueryParameter(array, columnType))
