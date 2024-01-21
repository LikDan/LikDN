package com.likdn.utils

private val charLowercasePool = 'a'..'z'
private val charUppercasePool = 'A'..'Z'
private val charNumbersPool = '0'..'9'

private val charPool = arrayOf(charLowercasePool, charUppercasePool, charNumbersPool)

fun randomString(to: Int = 64) = (0 until to)
    .joinToString("") { charPool.random().random().toString() }