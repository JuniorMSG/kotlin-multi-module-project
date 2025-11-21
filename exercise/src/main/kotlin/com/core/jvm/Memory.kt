package com.core.jvm

/**
 * 🔬 Immutable 객체의 메모리 주소 변화 실험
 *
 * 실행 방법:
 * 1. Kotlin Playground에서 실행: https://play.kotlinlang.org/
 * 2. IntelliJ에서 main 함수 실행
 * 3. 터미널에서: kotlinc ImmutableMemoryTest.kt -include-runtime -d test.jar && java -jar test.jar
 */

import java.math.BigDecimal

fun main() {
    println("=".repeat(80))
    println("🔬 Immutable 객체의 메모리 주소 변화 실험")
    println("=".repeat(80))
    println()
	
    // 1️⃣ String의 plus() 함수
    testStringPlus()
    println()
	
    // 2️⃣ BigDecimal의 plus() 함수
    testBigDecimalPlus()
    println()
	
    // 3️⃣ List의 plus() 함수
    testListPlus()
    println()
	
    // 4️⃣ Mutable vs Immutable 비교
    testMutableVsImmutable()
    println()
	
    // 5️⃣ 성능 비교
    testPerformance()
    println()
	
    // 6️⃣ 같은 값인데 다른 주소?
    testSameValueDifferentAddress()
}

/**
 * 1️⃣ String의 plus() 함수 테스트
 */
fun testStringPlus() {
    println("📝 1. String의 plus() 함수 테스트")
    println("-".repeat(80))
	
    var str = "Hello"
    println("원본 str = \"$str\"")
    println("원본 메모리 주소: ${System.identityHashCode(str)} (0x${Integer.toHexString(System.identityHashCode(str))})")
    println()
	
    // plus() 호출
    str = str.plus(" World")
    println("변경 후 str = \"$str\"")
    println("변경 후 메모리 주소: ${System.identityHashCode(str)} (0x${Integer.toHexString(System.identityHashCode(str))})")
    println()
	
    // 또 한 번 plus() 호출
    val originalAddress = System.identityHashCode(str)
    str = str + "!"
    println("다시 변경 str = \"$str\"" + originalAddress)
    println("다시 변경 후 메모리 주소: ${System.identityHashCode(str)} (0x${Integer.toHexString(System.identityHashCode(str))})")
    println()
	
    println("✅ 결론: plus() 호출 시마다 새로운 메모리 주소에 새 객체 생성!")
}

/**
 * 2️⃣ BigDecimal의 plus() 함수 테스트
 */
fun testBigDecimalPlus() {
    println("💰 2. BigDecimal의 plus() 함수 테스트")
    println("-".repeat(80))
	
    var num = BigDecimal("100.00")
    println("원본 num = $num")
    println("원본 메모리 주소: ${System.identityHashCode(num)} (0x${Integer.toHexString(System.identityHashCode(num))})")
    println()
	
    // plus() 호출
    num = num.plus(BigDecimal("50.00"))
    println("변경 후 num = $num")
    println("변경 후 메모리 주소: ${System.identityHashCode(num)} (0x${Integer.toHexString(System.identityHashCode(num))})")
    println()
	
    // 또 한 번 plus() 호출
    num = num + BigDecimal("25.00")
    println("다시 변경 num = $num")
    println("다시 변경 후 메모리 주소: ${System.identityHashCode(num)} (0x${Integer.toHexString(System.identityHashCode(num))})")
    println()
	
    println("✅ 결론: BigDecimal도 매번 새로운 객체 생성!")
}

/**
 * 3️⃣ List의 plus() 함수 테스트
 */
fun testListPlus() {
    println("📋 3. List의 plus() 함수 테스트")
    println("-".repeat(80))
	
    var list = listOf(1, 2, 3)
    println("원본 list = $list")
    println("원본 메모리 주소: ${System.identityHashCode(list)} (0x${Integer.toHexString(System.identityHashCode(list))})")
    println()
	
    // plus() 호출
    list = list.plus(4)
    println("변경 후 list = $list")
    println("변경 후 메모리 주소: ${System.identityHashCode(list)} (0x${Integer.toHexString(System.identityHashCode(list))})")
    println()
	
    // 또 한 번 plus() 호출
    list = list + 5
    println("다시 변경 list = $list")
    println("다시 변경 후 메모리 주소: ${System.identityHashCode(list)} (0x${Integer.toHexString(System.identityHashCode(list))})")
    println()
	
    println("✅ 결론: List도 매번 새로운 객체 생성!")
}

/**
 * 4️⃣ Mutable vs Immutable 비교
 */
fun testMutableVsImmutable() {
    println("🆚 4. Mutable vs Immutable 비교")
    println("-".repeat(80))
	
    // Immutable String
    println("❌ Immutable String (매번 새 객체 생성)")
    var str = "A"
    val addresses = mutableListOf<Int>()
	
    addresses.add(System.identityHashCode(str))
    println("초기: str = \"$str\", 주소 = ${addresses.last()}")
	
    str = str + "B"
    addresses.add(System.identityHashCode(str))
    println("추가1: str = \"$str\", 주소 = ${addresses.last()}")
	
    str = str + "C"
    addresses.add(System.identityHashCode(str))
    println("추가2: str = \"$str\", 주소 = ${addresses.last()}")
	
    println("주소 변화: ${addresses.joinToString(" → ")}")
    println("모든 주소가 다름! ❌ (${addresses.distinct().size}개의 서로 다른 객체)")
    println()
	
    // Mutable StringBuilder
    println("✅ Mutable StringBuilder (같은 객체 수정)")
    val sb = StringBuilder("A")
    val sbAddresses = mutableListOf<Int>()
	
    sbAddresses.add(System.identityHashCode(sb))
    println("초기: sb = \"$sb\", 주소 = ${sbAddresses.last()}")
	
    sb.append("B")
    sbAddresses.add(System.identityHashCode(sb))
    println("추가1: sb = \"$sb\", 주소 = ${sbAddresses.last()}")
	
    sb.append("C")
    sbAddresses.add(System.identityHashCode(sb))
    println("추가2: sb = \"$sb\", 주소 = ${sbAddresses.last()}")
	
    println("주소 변화: ${sbAddresses.joinToString(" → ")}")
    println("모든 주소가 같음! ✅ (${sbAddresses.distinct().size}개의 객체만 존재)")
}

/**
 * 5️⃣ 성능 비교
 */
fun testPerformance() {
    println("⚡ 5. 성능 비교 (10,000번 반복)")
    println("-".repeat(80))
	
    val iterations = 10_000
	
    // Immutable String
    val immutableStart = System.currentTimeMillis()
    var str = ""
    repeat(iterations) {
        str = str + "A"
    }
    val immutableTime = System.currentTimeMillis() - immutableStart
	
    println("❌ Immutable String: ${immutableTime}ms")
    println("   - 메모리 낭비 심함")
    println()
	
    // Mutable StringBuilder
    val mutableStart = System.currentTimeMillis()
    val sb = StringBuilder()
    repeat(iterations) {
        sb.append("A")
    }
    val result = sb.toString()
    val mutableTime = System.currentTimeMillis() - mutableStart
	
    println("✅ Mutable StringBuilder: ${mutableTime}ms")
    println("   - 1개의 StringBuilder 객체만 수정")
    println("   - 메모리 효율적" + result)
    println()
	
    val speedup = if (mutableTime > 0) immutableTime.toDouble() / mutableTime else Double.POSITIVE_INFINITY
    println("🚀 StringBuilder가 약 ${String.format("%.1f", speedup)}배 빠름!")
}

/**
 * 6️⃣ 같은 값인데 다른 주소인지 확인
 */
fun testSameValueDifferentAddress() {
    println("🔍 6. 같은 값, 다른 주소 확인")
    println("-".repeat(80))
	
    // 같은 값을 다른 방식으로 생성
    val str1 = "Hello"
    val str2 = "Hello"
    val str3 = "Hel" + "lo"
    val str4 = String("Hello".toCharArray())
	
    println("str1 = \"$str1\", 주소 = ${System.identityHashCode(str1)}")
    println("str2 = \"$str2\", 주소 = ${System.identityHashCode(str2)}")
    println("str3 = \"$str3\", 주소 = ${System.identityHashCode(str3)}")
    println("str4 = \"$str4\", 주소 = ${System.identityHashCode(str4)}")
    println()
	
    println("str1 == str2: ${str1 == str2} (값 비교)")
    println("str1 === str2: ${str1 === str2} (참조 비교)")
    println()
	
    println("str1 == str4: ${str1 == str4} (값 비교)")
    println("str1 === str4: ${str1 === str4} (참조 비교)")
    println()
	
    println("💡 String Pool 때문에 리터럴은 같은 주소를 가질 수 있지만,")
    println("   plus() 같은 연산으로 생성된 객체는 항상 새로운 주소!")
    println()
	
    // plus()로 생성한 경우
    val str5 = "Hello"
    val str6 = str5 + "" // 빈 문자열 더하기
	
    println("str5 = \"$str5\", 주소 = ${System.identityHashCode(str5)}")
    println("str6 = \"$str6\" (str5 + \"\"), 주소 = ${System.identityHashCode(str6)}")
    println("str5 === str6: ${str5 === str6} (참조 비교)")
    println()
    println("✅ plus() 연산은 같은 값이어도 새로운 객체 생성!")
}
