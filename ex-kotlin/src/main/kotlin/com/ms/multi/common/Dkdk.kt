package com.ms.multi.common

fun main() {
    println("'${emptyList<String>().joinToString(" / ").isEmpty()}'")  // ''
    println("'${listOf("A").joinToString(" / ")}'")          // 'A'
    println("'${listOf("A", "B").joinToString(" / ")}'")     // 'A / B'


    require(true) { print("5>3") }
    require(false) { print("5<3") }
}
