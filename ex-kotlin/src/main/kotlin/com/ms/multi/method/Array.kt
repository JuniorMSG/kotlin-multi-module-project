package com.ms.multi.method

import com.ms.multi.common.printFooter
import com.ms.multi.common.printHeader

fun main() {
    forEachEx()
    mapEx()
    filterEx()
    sumOfEx()
    reduceEx()
    foldEx()
    groupByEx()
    partitionEx()
    takeDropEx()
    zipEx()
    flatMapEx()
    distinctEx()
    sortedEx()
    associateEx()
    chunkedEx()
    windowedEx()
    anyAllNoneEx()
    findEx()
    countEx()
    maxMinEx()
}

// 1. forEach - 각 요소 순회
fun forEachEx() {
    printHeader("1. forEach - 각 요소 순회")
    listOf(1, 2, 3).forEach {
        println("요소: $it")
    }
    printFooter()
}

// 2. map - 변환
fun mapEx() {
    printHeader("2. map - 변환")
    val numbers = listOf(1, 2, 3, 4)
    val doubled =
        numbers.map {
            println("$it -> ${it * 2}")
            it * 2
        }
    println("결과: $doubled")
    printFooter()
}

// 3. filter - 필터링
fun filterEx() {
    printHeader("3. filter - 필터링")
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    val evens =
        numbers.filter {
            val isEven = it % 2 == 0
            println("$it -> 짝수? $isEven")
            isEven
        }
    println("짝수만: $evens")
    printFooter()
}

// 4. sumOf - 합계 계산
fun sumOfEx() {
    printHeader("4. sumOf - 합계 계산")
    val intArray = listOf(1, 2, 3, 4)
    val sumOf =
        intArray.sumOf {
            println("더하기: $it")
            it
        }
    println("합계: $sumOf")
    printFooter()
}

// 5. reduce - 누적 계산
fun reduceEx() {
    printHeader("5. reduce - 누적 계산")
    val numbers = listOf(1, 2, 3, 4)
    val product =
        numbers.reduce { acc, i ->
            println("$acc * $i = ${acc * i}")
            acc * i
        }
    println("곱셈 결과: $product")
    printFooter()
}

// 6. fold - 초기값과 함께 누적
fun foldEx() {
    printHeader("6. fold - 초기값과 함께 누적")
    val numbers = listOf(1, 2, 3, 4)
    val result =
        numbers.fold(100) { acc, i ->
            println("$acc + $i = ${acc + i}")
            acc + i
        }
    println("최종 결과 (초기값 100): $result")
    printFooter()
}

// 7. groupBy - 그룹화
fun groupByEx() {
    printHeader("7. groupBy - 그룹화")
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    val grouped =
        numbers.groupBy {
            val group = if (it % 2 == 0) "짝수" else "홀수"
            println("$it -> $group")
            group
        }
    println("그룹화 결과: $grouped")
    printFooter()
}

// 8. partition - 분할
fun partitionEx() {
    printHeader("8. partition - 분할")
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    val (evens, odds) =
        numbers.partition {
            val isEven = it % 2 == 0
            println("$it -> ${if (isEven) "짝수" else "홀수"}")
            isEven
        }
    println("짝수: $evens")
    println("홀수: $odds")
    printFooter()
}

// 9. take / drop - 일부 가져오기/버리기
fun takeDropEx() {
    printHeader("9. take / drop - 일부 가져오기/버리기")
    val numbers = listOf(1, 2, 3, 4, 5)
    println("원본: $numbers")
    println("앞 3개 (take): ${numbers.take(3)}")
    println("뒤 2개 (drop 3): ${numbers.drop(3)}")
    println("마지막 2개 (takeLast): ${numbers.takeLast(2)}")
    printFooter()
}

// 10. zip - 두 리스트 결합
fun zipEx() {
    printHeader("10. zip - 두 리스트 결합")
    val numbers = listOf(1, 2, 3)
    val letters = listOf("a", "b", "c", "d")
    val zipped =
        numbers.zip(letters) { num, letter ->
            println("$num + $letter")
            "$num$letter"
        }
    println("결과: $zipped")
    printFooter()
}

// 11. flatMap - 평탄화 + 변환
fun flatMapEx() {
    printHeader("11. flatMap - 평탄화 + 변환")
    val numbers = listOf(1, 2, 3)
    val result =
        numbers.flatMap {
            println("$it -> [$it, ${it * 10}]")
            listOf(it, it * 10)
        }
    println("결과: $result")
    printFooter()
}

// 12. distinct - 중복 제거
fun distinctEx() {
    printHeader("12. distinct - 중복 제거")
    val numbers = listOf(1, 2, 2, 3, 3, 3, 4)
    println("원본: $numbers")
    val distinct = numbers.distinct()
    println("중복 제거: $distinct")

    val words = listOf("apple", "Banana", "APPLE", "banana")
    val distinctBy = words.distinctBy { it.lowercase() }
    println("대소문자 무시 중복 제거: $distinctBy")
    printFooter()
}

// 13. sorted - 정렬
fun sortedEx() {
    printHeader("13. sorted - 정렬")
    val numbers = listOf(3, 1, 4, 1, 5, 9, 2, 6)
    println("원본: $numbers")
    println("오름차순: ${numbers.sorted()}")
    println("내림차순: ${numbers.sortedDescending()}")

    val words = listOf("banana", "apple", "cherry")
    println("길이순 정렬: ${words.sortedBy { it.length }}")
    printFooter()
}

// 14. associate - Map 생성
fun associateEx() {
    printHeader("14. associate - Map 생성")
    val numbers = listOf(1, 2, 3, 4)
    val map =
        numbers.associate {
            println("$it -> ${it * it}")
            it to it * it
        }
    println("결과 Map: $map")

    val associateWith = numbers.associateWith { it * it }
    println("associateWith: $associateWith")
    printFooter()
}

// 15. chunked - 청크로 분할
fun chunkedEx() {
    printHeader("15. chunked - 청크로 분할")
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    println("원본: $numbers")
    val chunked = numbers.chunked(3)
    println("3개씩 분할: $chunked")

    val chunkedTransform =
        numbers.chunked(3) { chunk ->
            println("청크: $chunk -> 합: ${chunk.sum()}")
            chunk.sum()
        }
    println("각 청크의 합: $chunkedTransform")
    printFooter()
}

// 16. windowed - 슬라이딩 윈도우
fun windowedEx() {
    printHeader("16. windowed - 슬라이딩 윈도우")
    val numbers = listOf(1, 2, 3, 4, 5)
    println("원본: $numbers")
    val windowed = numbers.windowed(3)
    println("크기 3 윈도우: $windowed")

    val windowedStep = numbers.windowed(3, step = 2)
    println("크기 3, 스텝 2: $windowedStep")
    printFooter()
}

// 17. any / all / none - 조건 검사
fun anyAllNoneEx() {
    printHeader("17. any / all / none - 조건 검사")
    val numbers = listOf(1, 2, 3, 4, 5)
    println("원본: $numbers")
    println("짝수가 하나라도 있나? ${numbers.any { it % 2 == 0 }}")
    println("모두 양수인가? ${numbers.all { it > 0 }}")
    println("음수가 없나? ${numbers.none { it < 0 }}")
    printFooter()
}

// 18. find - 조건에 맞는 첫 요소
fun findEx() {
    printHeader("18. find - 조건에 맞는 첫 요소")
    val numbers = listOf(1, 2, 3, 4, 5)
    val firstEven = numbers.find { it % 2 == 0 }
    println("첫 번째 짝수: $firstEven")

    val firstGreaterThan10 = numbers.find { it > 10 }
    println("10보다 큰 첫 수: $firstGreaterThan10")
    printFooter()
}

// 19. count - 조건 카운트
fun countEx() {
    printHeader("19. count - 조건 카운트")
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    println("원본: $numbers")
    println("전체 개수: ${numbers.count()}")
    println("짝수 개수: ${numbers.count { it % 2 == 0 }}")
    println("3보다 큰 수: ${numbers.count { it > 3 }}")
    printFooter()
}

// 20. max / min - 최대/최소
fun maxMinEx() {
    printHeader("20. max / min - 최대/최소")
    val numbers = listOf(3, 1, 4, 1, 5, 9, 2, 6)
    println("원본: $numbers")
    println("최대값: ${numbers.maxOrNull()}")
    println("최소값: ${numbers.minOrNull()}")

    val words = listOf("apple", "banana", "cherry")
    println("가장 긴 단어: ${words.maxByOrNull { it.length }}")
    println("가장 짧은 단어: ${words.minByOrNull { it.length }}")
    printFooter()
}
