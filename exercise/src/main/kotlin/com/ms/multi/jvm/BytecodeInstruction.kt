package com.ms.multi.jvm

/**
 * JVM 바이트코드 수준의 메서드 호출 시뮬레이션
 */

sealed class BytecodeInstruction {
    data class NEW(
        val className: String,
    ) : BytecodeInstruction()

    data class ALOAD(
        val index: Int,
    ) : BytecodeInstruction() // 지역 변수 로드

    data class ASTORE(
        val index: Int,
    ) : BytecodeInstruction() // 지역 변수 저장

    data class GETFIELD(
        val fieldName: String,
    ) : BytecodeInstruction()

    data class GETSTATIC(
        val fieldName: String,
    ) : BytecodeInstruction()

    data class INVOKEVIRTUAL(
        val methodName: String,
    ) : BytecodeInstruction()

    data class INVOKESPECIAL(
        val methodName: String,
    ) : BytecodeInstruction()

    object RETURN : BytecodeInstruction()
}

// ============================================
// 바이트코드 실행 엔진
// ============================================

class BytecodeExecutor {
    private val operandStack = mutableListOf<Any?>()
    private val localVariables = mutableMapOf<Int, Any?>()

    fun execute(instructions: List<BytecodeInstruction>) {
        println("\n🔧 바이트코드 실행:\n")

        instructions.forEachIndexed { index, instruction ->
            println("   ${index + 1}. ${instruction.javaClass.simpleName} ${getInstructionDetails(instruction)}")

            when (instruction) {
                is BytecodeInstruction.NEW -> {
                    val obj = "Instance of ${instruction.className}"
                    operandStack.add(obj)
                    println("      → 스택에 추가: $obj")
                }
                is BytecodeInstruction.ALOAD -> {
                    val value = localVariables[instruction.index]
                    operandStack.add(value)
                    println("      → 지역변수[${instruction.index}]를 스택에 로드: $value")
                }
                is BytecodeInstruction.ASTORE -> {
                    val value = operandStack.removeLastOrNull()
                    localVariables[instruction.index] = value
                    println("      → 스택에서 꺼내 지역변수[${instruction.index}]에 저장: $value")
                }
                is BytecodeInstruction.GETFIELD -> {
                    val obj = operandStack.removeLastOrNull()
                    println("      → 객체 $obj 에서 필드 '${instruction.fieldName}' 읽기")
                    operandStack.add("value of ${instruction.fieldName}")
                }
                is BytecodeInstruction.GETSTATIC -> {
                    println("      → static 필드 '${instruction.fieldName}' 읽기")
                    operandStack.add("Companion.INSTANCE")
                }
                is BytecodeInstruction.INVOKEVIRTUAL -> {
                    val obj = operandStack.removeLastOrNull()
                    println("      → 객체 $obj 의 메서드 '${instruction.methodName}' 호출")
                    println("      → this = $obj")
                }
                is BytecodeInstruction.INVOKESPECIAL -> {
                    println("      → 생성자 호출")
                }
                BytecodeInstruction.RETURN -> {
                    println("      → 메서드 종료")
                }
            }
        }
    }

    private fun getInstructionDetails(instruction: BytecodeInstruction): String =
        when (instruction) {
            is BytecodeInstruction.NEW -> "\"${instruction.className}\""
            is BytecodeInstruction.ALOAD -> "#${instruction.index}"
            is BytecodeInstruction.ASTORE -> "#${instruction.index}"
            is BytecodeInstruction.GETFIELD -> "\"${instruction.fieldName}\""
            is BytecodeInstruction.GETSTATIC -> "\"${instruction.fieldName}\""
            is BytecodeInstruction.INVOKEVIRTUAL -> "\"${instruction.methodName}\""
            is BytecodeInstruction.INVOKESPECIAL -> "\"<init>\""
            BytecodeInstruction.RETURN -> ""
        }
}

// ============================================
// 바이트코드 예제
// ============================================

fun main() {
    println("=".repeat(60))
    println("📝 시나리오 1: 인스턴스 메서드 호출")
    println("=".repeat(60))
    println("\nKotlin 코드:")
    println("   val core = HelloCore()")
    println("   core.instanceHello()")

    val instanceMethodBytecode =
        listOf(
            BytecodeInstruction.NEW("HelloCore"),
            BytecodeInstruction.INVOKESPECIAL("<init>"),
            BytecodeInstruction.ASTORE(1),
            BytecodeInstruction.ALOAD(1),
            BytecodeInstruction.INVOKEVIRTUAL("instanceHello"),
            BytecodeInstruction.RETURN,
        )

    BytecodeExecutor().execute(instanceMethodBytecode)

    println("\n" + "=".repeat(60))
    println("📝 시나리오 2: Companion 메서드 호출")
    println("=".repeat(60))
    println("\nKotlin 코드:")
    println("   HelloCore.staticHello()")

    val companionMethodBytecode =
        listOf(
            BytecodeInstruction.GETSTATIC("HelloCore.Companion"),
            BytecodeInstruction.INVOKEVIRTUAL("staticHello"),
            BytecodeInstruction.RETURN,
        )

    BytecodeExecutor().execute(companionMethodBytecode)

    println("\n" + "=".repeat(60))
    println("🎯 핵심 차이점")
    println("=".repeat(60))
    println(
        """

        인스턴스 메서드:
        ├─ NEW 명령어로 객체 생성 필요
        ├─ ALOAD로 객체 참조를 스택에 로드
        └─ INVOKEVIRTUAL 호출 시 this = 로드한 객체

        Companion 메서드:
        ├─ NEW 불필요 (이미 존재)
        ├─ GETSTATIC으로 Companion.INSTANCE 가져오기
        └─ INVOKEVIRTUAL 호출 시 this = Companion.INSTANCE

        """.trimIndent(),
    )
}
