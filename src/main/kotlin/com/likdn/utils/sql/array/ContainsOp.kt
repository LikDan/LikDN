package com.likdn.utils.sql.array

import org.jetbrains.exposed.sql.ComparisonOp
import org.jetbrains.exposed.sql.Expression


class ContainsOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "@>")
