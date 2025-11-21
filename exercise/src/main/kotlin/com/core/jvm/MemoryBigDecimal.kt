package com.core.jvm

/**
 * 🔬 BigDecimal vs Int/Double 메모리 생성 실험
 * 1만번 반복 시 메모리 할당 패턴 비교
 *
 * Int	2.4ms	⚡ 기준	정수 연산, 카운터, 인덱스
 * Double	1918ms	🐢 800배 느림	과학 계산, 근사값
 * BigDecimal	5045ms	🐌 2092배 느림	금융 계산, 정확한 소수점
 */

import java.math.BigDecimal

fun main() {
    println("=".repeat(80))
    println("🔬 BigDecimal vs Primitive 타입 메모리 생성 비교")
    println("=".repeat(80))
    println()
	
    // 1️⃣ BigDecimal: 1만개의 객체 생성
    testBigDecimalLoop()
    println()
	
    // 2️⃣ Int (Primitive): 1개의 메모리 경로만 사용
    testIntLoop()
    println()
	
    // 3️⃣ Double (Primitive): 1개의 메모리 경로만 사용
    testDoubleLoop()
    println()
	
    // 4️⃣ 성능 비교
    performanceComparison()
    /**
     * 🔬 성능 차이의 근본 원인 3가지
     */

    var bigSum = BigDecimal.ZERO
    repeat(2_000_000_000) {
        bigSum = bigSum.add(BigDecimal.ONE) // 매번 새 객체 생성!
        // ↓
        // new BigDecimal() → Heap 할당 → GC 대상
    }
// 결과: 5045ms (2092배 느림)

// 2️⃣ Double: CPU FPU 연산 (중간) 🐢
    var doubleSum = 0.0
    repeat(2_000_000_000) {
        doubleSum += 1.0 // FPU(부동소수점 유닛) 사용
        // ↓
        // 지수 맞추기 → 가수 덧셈 → 정규화 → 반올림
    }
// 결과: 1918ms (800배 느림)

// 3️⃣ Int: CPU ALU 직접 연산 (가장 빠름) ⚡
    var intSum = 0
    repeat(2_000_000_000) {
        intSum += 1 // CPU 정수 연산 유닛(ALU) 직접 사용
        // ↓
        // 단순 ADD 명령어 1개
    }
// 결과: 2.4ms (기준)
}

/**
 * 1️⃣ BigDecimal: 매번 새로운 객체 생성
 */
fun testBigDecimalLoop() {
    println("💰 1. BigDecimal (Immutable 객체)")
    println("-".repeat(80))
	
    var sum = BigDecimal.ZERO
    val addresses = mutableListOf<String>()
	
    println("🔄 10,000번 반복 시작... 1.")
    repeat(10_000) { i ->
        sum = sum.add(BigDecimal.ONE) // ❌ 매번 새로운 BigDecimal 객체 생성!
		
        // 처음 5개만 메모리 주소 출력
        if (i < 5) {
            val address = System.identityHashCode(sum).toString(16)
            addresses.add(address)
            println("  반복 ${i + 1}: sum = $sum, 메모리 주소 = 0x$address")
        }
    }
	
    println()
    println("📊 결과 분석: 1")
    println("   - 총 생성된 객체 수: 약 10,000개 ❌")
    println("   - 메모리 위치: Heap (각각 다른 주소)")
    println("   - 메모리 주소 변화:")
    addresses.forEachIndexed { index, addr ->
        println("      반복 ${index + 1}: 0x$addr")
    }
    println("   - GC 부담: 매우 높음 🔥")
    println("   - 성능: 느림 🐢")
}

/**
 * 2️⃣ Int: 단일 메모리 경로에서 값만 변경
 */
fun testIntLoop() {
    println("⚡ 2. Int (Primitive 타입)")
    println("-".repeat(80))
	
    var sum = 0
	
    println("🔄 10,000번 반복 시작... . 2")
    println("   메모리 위치: Stack (고정)")
    println("   메모리 주소: 변하지 않음 ✅")
	
    repeat(10_000) { i ->
        sum = sum + 1 // ✅ 같은 메모리 위치에서 값만 변경!
		
        if (i < 5) {
            println("  반복 ${i + 1}: sum = $sum")
        }
    }
	
    println()
    println("📊 결과 분석: 2 ")
    println("   - 총 생성된 객체 수: 0개 ✅")
    println("   - 메모리 위치: Stack (단일 경로)")
    println("   - 메모리 주소: 변하지 않음 (같은 위치에서 값만 변경)")
    println("   - GC 부담: 없음 ✅")
    println("   - 성능: 매우 빠름 ⚡")
}

/**
 * 3️⃣ Double: 단일 메모리 경로에서 값만 변경
 */
fun testDoubleLoop() {
    println("⚡ 3. Double (Primitive 타입)")
    println("-".repeat(80))
	
    var sum = 0.0
	
    println("🔄 10,000번 반복 시작...3")
    println("   메모리 위치: Stack (고정)")
    println("   메모리 주소: 변하지 않음 ✅")
	
    repeat(10_000) { i ->
        sum = sum + 1.0 // ✅ 같은 메모리 위치에서 값만 변경!
		
        if (i < 5) {
            println("  반복 ${i + 1}: sum = $sum")
        }
    }
	
    println()
    println("📊 결과 분석: 3 ")
	  println("   - 총 생성된 객체 수: 0개 ✅")
    println("   - 메모리 위치: Stack (단일 경로)")
    println("   - 메모리 주소: 변하지 않음")
    println("   - GC 부담: 없음 ✅")
    println("   - 성능: 매우 빠름 ⚡")
}

/**
 * 4️⃣ 성능 비교 실험
 */
fun performanceComparison() {
    println("🏁 4. 성능 비교 (100,000번 반복)")
    println("-".repeat(80))
	
    val iterations = 2000000000
	
    // BigDecimal 성능 측정
    println("💰 BigDecimal 측정 중...")
    var bigDecimalSum = BigDecimal.ZERO
    val bigDecimalStart = System.nanoTime()
	
    repeat(iterations) {
        bigDecimalSum = bigDecimalSum.add(BigDecimal.ONE)
    }
	
    val bigDecimalTime = (System.nanoTime() - bigDecimalStart) / 1_000_000.0
    println("   시간: ${bigDecimalTime}ms")
    println("   결과: $bigDecimalSum")
    println()
	
    // Int 성능 측정
    println("⚡ Int 측정 중...")
    var intSum = 0
    val intStart = System.nanoTime()
	
    repeat(iterations) {
        intSum = intSum + 1
    }
	
    val intTime = (System.nanoTime() - intStart) / 1_000_000.0
    println("   시간: ${intTime}ms")
    println("   결과: $intSum")
    println()
	
    // Double 성능 측정
    println("⚡ Double 측정 중...")
    var doubleSum = 0.0
    val doubleStart = System.nanoTime()
	
    repeat(iterations) {
        doubleSum = doubleSum + 1.0
    }
	
    val doubleTime = (System.nanoTime() - doubleStart) / 1_000_000.0
    println("   시간: ${doubleTime}ms")
    println("   결과: $doubleSum")
    println()
	
    // 비교 결과
    println("📊 성능 비교 결과:")
    println("-".repeat(80))
    println("   BigDecimal: ${bigDecimalTime}ms (기준)")
    println("   Int:        ${intTime}ms (약 ${String.format("%.0f", bigDecimalTime / intTime)}배 빠름)")
    println("   Double:     ${doubleTime}ms (약 ${String.format("%.0f", bigDecimalTime / doubleTime)}배 빠름)")
    println()
	
    println("💡 결론:")
    println("   - BigDecimal은 매번 새로운 객체를 생성하므로 매우 느림")
    println("   - Int/Double은 Stack에서 직접 연산하므로 매우 빠름")
    println("   - 성능 차이: 수십~수백 배!")
}
