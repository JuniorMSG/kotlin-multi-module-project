package com.core

class HelloCore {
    fun hello() {
        println("Hello, Core! - Instance")
    }

    companion object {
        fun hello() {
            println("Hello, Core!")
        }
    }
}
