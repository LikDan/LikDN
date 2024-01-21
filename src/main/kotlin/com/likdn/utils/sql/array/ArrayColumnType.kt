package com.likdn.utils.sql.array

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.postgresql.jdbc.PgArray

class ArrayColumnType<T : Any>(private val base: Column<T>) : ColumnType() {
    override fun sqlType(): String = base.columnType.sqlType() + "[]"

    override fun valueFromDB(value: Any): Any = when (value) {
        is ArrayList<*> -> value.map { this.base.columnType.valueFromDB(it) }
        is PgArray -> (value.array as Array<*>).map { this.base.columnType.valueFromDB(it!!) }
        else -> error("$value of ${value::class.qualifiedName} is not valid for ArrayList")
    }

    @Suppress("UNCHECKED_CAST")
    override fun notNullValueToDB(value: Any): Array<Any> = when (value) {
        is ArrayList<*> -> value.map { this.base.columnType.notNullValueToDB(it) as T }.toTypedArray()
        else -> error("$value of ${value::class.qualifiedName} is not valid for ArrayList")
    }

    override fun valueToString(value: Any?): String =
        value?.let { this.notNullValueToDB(it).joinToString(prefix = "{", postfix = "}") } ?: "{}"


    @Suppress("UNCHECKED_CAST")
    private fun List<T>.toTypedArray(): Array<Any> {
        if (this.isEmpty())
            return java.lang.reflect.Array.newInstance(String::class.java, 0) as Array<String> as Array<Any>

        val thisCollection = this as java.util.Collection<T>
        val instance = java.lang.reflect.Array.newInstance(get(0)::class.java, 0) as Array<T>
        return thisCollection.toArray(instance) as Array<T> as Array<Any>
    }
}
