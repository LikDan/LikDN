package com.likdn.app.storage

import com.likdn.app.customers.CustomersEntry
import java.io.File

fun CustomersEntry.file(filename: String) = File("storage/${this.id}/", filename).apply { parentFile.mkdirs() }
fun CustomersEntry.dir() = File("storage/${this.id}/").apply { mkdirs() }

fun generateFileName(short: Boolean = false): String {
    val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return List(if (short) 5 else 200) { alphabet.random() }.joinToString("")
}