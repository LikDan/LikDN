package com.likdn.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.currentDialect
import org.postgresql.jdbc.PgArray
import kotlin.reflect.KClass

inline fun <reified T : Enum<T>> Table.enumerationArray(name: String): Column<ArrayList<T>> =
    registerColumn(name, EnumArrayColumnType(T::class))

class EnumArrayColumnType<T : Enum<T>>(private val klass: KClass<T>) : ColumnType() {
    private val enumConstants by lazy { klass.java.enumConstants!! }

    override fun sqlType() = currentDialect.dataTypeProvider.integerType() + "[]"

    @Suppress("UNCHECKED_CAST")
    override fun valueFromDB(value: Any): List<T> = when (value) {
        is ArrayList<*> -> value.map {
            when (it) {
                is Number -> enumConstants[it.toInt()]
                is Enum<*> -> it as T
                else -> error("$it of ${it::class.qualifiedName} is not valid for enum ${klass.simpleName}")
            }
        }

        is PgArray -> (value.array as Array<Int>).map { enumConstants[it] }

        else -> error("$value of ${value::class.qualifiedName} is not valid for ArrayList")
    }

    override fun notNullValueToDB(value: Any): Array<Int> =
        when (value) {
            is ArrayList<*> -> value.map {
                when (it) {
                    is Int -> it
                    is Enum<*> -> it.ordinal
                    else -> error("$it of ${it::class.qualifiedName} is not valid for enum ${klass.simpleName}")
                }
            }.toTypedArray()

            else -> error("$value of ${value::class.qualifiedName} is not valid for ArrayList")
        }

    override fun valueToString(value: Any?): String {
        return "{${this.notNullValueToDB(value ?: return "{}").joinToString()}}"
    }
}