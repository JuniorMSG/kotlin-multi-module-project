package com.core.jvm

/**
 * 실제 메모리 주소를 시뮬레이션하여
 * this 참조가 어떻게 전달되는지 보여주는 코드
 */

class MemorySimulator {
    private var nextAddress = 0x1000
    private val memory = mutableMapOf<Int, Any>()

    fun allocate(data: Any): Int {
        val address = nextAddress
        nextAddress += 0x10
        memory[address] = data
        return address
    }

    fun read(address: Int): Any? = memory[address]

    fun formatAddress(address: Int): String = "0x${address.toString(16).uppercase()}"
}

data class ObjectData(
    val className: String,
    val fields: MutableMap<String, Any>,
)

fun main() {
    val memory = MemorySimulator()
	
    println("=".repeat(60))
    println("🧠 메모리 주소 레벨 시뮬레이션")
    println("=".repeat(60))
	
    // 1. Companion 객체 생성 (클래스 로딩 시)
    println("\n📦 1단계: 클래스 로딩 (JVM 시작 시)\n")
	
    val companionAddress =
        memory.allocate(
            ObjectData("HelloCore\$Companion", mutableMapOf("staticMessage" to "Hello, Static!")),
        )
	
    println("   Method Area에 저장:")
    println("   ├─ HelloCore 클래스 메타데이터")
    println("   └─ Companion 싱글톤 인스턴스")
    println("      주소: ${memory.formatAddress(companionAddress)}")
    println("      데이터: ${memory.read(companionAddress)}")
	
    // 2. 인스턴스 객체 생성
    println("\n🏗️  2단계: 객체 생성 (val core = HelloCore())\n")
	
    val instanceAddress =
        memory.allocate(
            ObjectData("HelloCore", mutableMapOf("message" to "Hello, Core!")),
        )
	
    println("   Heap에 저장:")
    println("   주소: ${memory.formatAddress(instanceAddress)}")
    println("   데이터: ${memory.read(instanceAddress)}")
	
    // 3. 인스턴스 메서드 호출
    println("\n📞 3단계: 인스턴스 메서드 호출 (core.instanceHello())\n")
	
    println("   메서드 시그니처:")
    println("   fun instanceHello(this: HelloCore)  // 숨겨진 파라미터!")
    println()
    println("   실제 호출:")
    println("   instanceHello(this = ${memory.formatAddress(instanceAddress)})")
    println()
    println("   메서드 내부:")
    println("   ├─ this 참조 = ${memory.formatAddress(instanceAddress)}")
    println("   ├─ this.message 읽기")
    println("   │  └─ 주소 ${memory.formatAddress(instanceAddress)}에서 'message' 필드 찾기")
    println("   │     → \"Hello, Core!\"")
    println("   └─ 출력: Hello, Core!")
	
    // 4. Companion 메서드 호출
    println("\n📞 4단계: Companion 메서드 호출 (HelloCore.staticHello())\n")
	
    println("   메서드 시그니처:")
    println("   fun staticHello(this: HelloCore\$Companion)")
    println()
    println("   실제 호출:")
    println("   staticHello(this = ${memory.formatAddress(companionAddress)})")
    println()
    println("   메서드 내부:")
    println("   ├─ this 참조 = ${memory.formatAddress(companionAddress)}")
    println("   ├─ this.staticMessage 읽기")
    println("   │  └─ 주소 ${memory.formatAddress(companionAddress)}에서 'staticMessage' 필드 찾기")
    println("   │     → \"Hello, Static!\"")
    println("   └─ 출력: Hello, Static!")
	
    // 5. 비교
    println("\n" + "=".repeat(60))
    println("📊 메모리 맵")
    println("=".repeat(60))
    println(
        """
        
        Method Area (클래스 로딩 시 생성):
        ┌────────────────────────────────────────┐
        │ ${memory.formatAddress(companionAddress)}  HelloCore${'$'}Companion    │ ← 항상 존재!
        │         └─ staticMessage: "Hello..."   │
        └────────────────────────────────────────┘
        
        Heap (객체 생성 시):
        ┌────────────────────────────────────────┐
        │ ${memory.formatAddress(instanceAddress)}  HelloCore                  │ ← new 호출 시 생성
        │         └─ message: "Hello, Core!"     │
        └────────────────────────────────────────┘
        
        """.trimIndent(),
    )
	
    println("=".repeat(60))
    println("💡 핵심 깨달음")
    println("=".repeat(60))
    println(
        """
        
        1. 모든 메서드는 this 참조가 필요하다
           → 인스턴스 메서드든 companion 메서드든 동일!
        
        2. 차이점은 this가 가리키는 메모리 주소
           → 인스턴스 메서드: Heap의 객체 주소
           → Companion 메서드: Method Area의 Companion 주소
        
        3. Companion은 클래스 로딩 시 이미 생성됨
           → 객체 생성 없이도 유효한 this 참조 존재
        
        4. 인스턴스는 명시적으로 생성해야 함
           → 객체 없으면 this = null → 호출 불가!
        
        """.trimIndent(),
    )
}
