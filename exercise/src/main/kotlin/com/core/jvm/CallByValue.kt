package com.core.jvm

data class Person(
    val name: String,
)

fun main() {
    var a = Person("1234")
    var b = a // 주소값 복사
	
    println("=== 초기 상태 ===")
    println("a.name: ${a.name}") // "1234"
    println("b.name: ${b.name}") // "1234"
    println("a === b: ${a === b}") // true (같은 객체)
	
    // a를 재할당
    a = Person("3456")
	
    println("\n=== a 재할당 후 ===")
    println("a.name: ${a.name}") // "3456" ✅
    println("b.name: ${b.name}") // "1234" ✅ (변하지 않음!)
    println("a === b: ${a === b}") // false (다른 객체)
}
