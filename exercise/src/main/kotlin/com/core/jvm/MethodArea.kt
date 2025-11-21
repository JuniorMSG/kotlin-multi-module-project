package com.core.jvm

/**
 * JVM 메모리 구조를 시뮬레이션하는 코드
 * 실제 JVM이 어떻게 동작하는지 이해하기 위한 예제
 *
 *  Method Area (메서드 영역)
 *  - 클래스 메타데이터, static 변수/메서드 저장
 */

object MethodArea {
    // 클래스 정보를 저장하는 맵
    private val classMetadata = mutableMapOf<String, ClassInfo>()

    fun loadClass(
        className: String,
        classInfo: ClassInfo,
    ) {
        println("📦 [Method Area] 클래스 로딩: $className")
        classMetadata[className] = classInfo
		
        // companion object가 있으면 즉시 초기화
        classInfo.companionObject?.let {
            println("   ✅ Companion object 초기화: ${it.name}")
        }
    }

    fun getClass(className: String): ClassInfo? = classMetadata[className]
}

/**
 * Heap (힙 영역)
 * - 객체 인스턴스 저장
 */
object Heap {
    private val objects = mutableMapOf<Int, ObjectInstance>()
    private var nextObjectId = 1

    fun allocate(
        className: String,
        instanceVariables: Map<String, Any>,
    ): ObjectInstance {
        val objectId = nextObjectId++
        val obj = ObjectInstance(objectId, className, instanceVariables.toMutableMap())
        objects[objectId] = obj
        println("🏗️  [Heap] 객체 생성: $className@$objectId")
        return obj
    }

    fun getObject(objectId: Int): ObjectInstance? = objects[objectId]
}

/**
 * Stack (스택 영역)
 * - 메서드 호출 정보, 지역 변수 저장
 */
object Stack {
    private val callStack = mutableListOf<StackFrame>()

    fun push(
        methodName: String,
        thisReference: ObjectInstance?,
        localVariables: Map<String, Any> = emptyMap(),
    ) {
        val frame = StackFrame(methodName, thisReference, localVariables.toMutableMap())
        callStack.add(frame)
        println("📚 [Stack] 메서드 호출: $methodName (this=${thisReference?.let { "객체@${it.objectId}" } ?: "null"})")
    }

    fun pop(): StackFrame? {
        if (callStack.isEmpty()) return null
        val frame = callStack.removeLast()
        println("📤 [Stack] 메서드 종료: ${frame.methodName}")
        return frame
    }

    fun getCurrentFrame(): StackFrame? = callStack.lastOrNull()
}

// ============================================
// 데이터 구조
// ============================================

data class ClassInfo(
    val className: String,
    val instanceMethods: Map<String, MethodInfo>,
    val companionObject: CompanionObjectInfo? = null,
)

data class MethodInfo(
    val methodName: String,
    val needsThisReference: Boolean, // 핵심! this가 필요한가?
    val implementation: (ObjectInstance?) -> String,
)

data class CompanionObjectInfo(
    val name: String,
    val instance: ObjectInstance, // 싱글톤 인스턴스
    val methods: Map<String, MethodInfo>,
)

data class ObjectInstance(
    val objectId: Int,
    val className: String,
    val variables: MutableMap<String, Any>,
) {
    override fun toString() = "$className@$objectId"
}

data class StackFrame(
    val methodName: String,
    val thisReference: ObjectInstance?, // 핵심! this 참조
    val localVariables: MutableMap<String, Any>,
)

// ============================================
// 실제 클래스 정의 (Kotlin 코드)
// ============================================

class HelloCore {
    var message: String = "Hello, Core!" // 인스턴스 변수

    // 인스턴스 메서드
    fun instanceHello() {
        println(message) // this.message에 접근
    }

    companion object {
        var staticMessage: String = "Hello, Static!" // companion 변수

        // companion 메서드
        fun staticHello() {
            println(staticMessage)
        }
    }
}

// ============================================
// JVM 시뮬레이터
// ============================================

object JVMSimulator {
    fun start() {
        println("\n" + "=".repeat(60))
        println("🚀 JVM 시작 - 클래스 로딩 단계")
        println("=".repeat(60) + "\n")
		
        // 1. HelloCore 클래스를 Method Area에 로드
        loadHelloCoreClass()
		
        println("\n" + "=".repeat(60))
        println("💡 시나리오 1: 인스턴스 메서드 호출 (객체 없이)")
        println("=".repeat(60) + "\n")
		
        // 2. 인스턴스 메서드를 객체 없이 호출 시도
        tryCallInstanceMethodWithoutObject()
		
        println("\n" + "=".repeat(60))
        println("💡 시나리오 2: 인스턴스 메서드 호출 (객체 생성 후)")
        println("=".repeat(60) + "\n")
		
        // 3. 객체 생성 후 인스턴스 메서드 호출
        callInstanceMethodWithObject()
		
        println("\n" + "=".repeat(60))
        println("💡 시나리오 3: Companion 메서드 호출")
        println("=".repeat(60) + "\n")
		
        // 4. Companion object 메서드 호출
        callCompanionMethod()
    }

    private fun loadHelloCoreClass() {
        // Companion object 싱글톤 인스턴스 생성
        val companionInstance =
            Heap.allocate(
                "HelloCore\$Companion",
                mapOf("staticMessage" to "Hello, Static!"),
            )
		
        // Companion object 메서드 정의
        val companionMethods =
            mapOf(
                "staticHello" to
                    MethodInfo(
                        methodName = "staticHello",
                        needsThisReference = true, // Companion 인스턴스 필요
                        implementation = { thisRef ->
                            val msg = thisRef?.variables?.get("staticMessage") as? String
                            "   🎤 출력: $msg"
                        },
                    ),
            )
		
        // 인스턴스 메서드 정의
        val instanceMethods =
            mapOf(
                "instanceHello" to
                    MethodInfo(
                        methodName = "instanceHello",
                        needsThisReference = true, // 인스턴스 필요!
                        implementation = { thisRef ->
                            if (thisRef == null) {
                                throw NullPointerException("❌ this 참조가 null입니다!")
                            }
                            val msg = thisRef.variables["message"] as? String
                            "   🎤 출력: $msg"
                        },
                    ),
            )
		
        // 클래스 정보 생성
        val classInfo =
            ClassInfo(
                className = "HelloCore",
                instanceMethods = instanceMethods,
                companionObject =
                    CompanionObjectInfo(
                        name = "Companion",
                        instance = companionInstance,
                        methods = companionMethods,
                    ),
            )
		
        // Method Area에 로드
        MethodArea.loadClass("HelloCore", classInfo)
    }

    private fun tryCallInstanceMethodWithoutObject() {
        println("🔍 코드: HelloCore.instanceHello()  // 객체 없이 호출 시도\n")
		
        val classInfo = MethodArea.getClass("HelloCore")!!
        val method = classInfo.instanceMethods["instanceHello"]!!
		
        println("⚙️  JVM 내부 동작 1 :")
        println("   1. Method Area에서 'HelloCore' 클래스 찾기 ✅ 1 ")
        println("   2. 'instanceHello' 메서드 찾기 ✅")
        println("   3. 메서드가 this 참조 필요? ${method.needsThisReference}")
		
        if (method.needsThisReference) {
            println("   4. this 참조 찾기... ❌")
            println("\n❌ 컴파일 에러: Unresolved reference: instanceHello")
            println("   → 인스턴스 메서드는 객체 없이 호출할 수 없습니다!")
        }
    }

    private fun callInstanceMethodWithObject() {
        println("🔍 코드:")
        println("   val core = HelloCore()  // 객체 생성")
        println("   core.instanceHello()    // 메서드 호출\n")
		
        // 1. 객체 생성 (힙에 할당)
        val coreInstance =
            Heap.allocate(
                "HelloCore",
                mapOf("message" to "Hello, Core!"),
            )
		
        println()
		
        // 2. 메서드 호출
        val classInfo = MethodArea.getClass("HelloCore")!!
        val method = classInfo.instanceMethods["instanceHello"]!!
		
        println("⚙️  JVM 내부 동작 2:")
        println("   1. Method Area에서 'HelloCore' 클래스 찾기 ✅ 2 ")
        println("   2. 'instanceHello' 메서드 찾기 ✅")
        println("   3. 메서드가 this 참조 필요? ${method.needsThisReference}")
        println("   4. this 참조 = 객체@${coreInstance.objectId} ✅")
		
        // 3. 스택 프레임 생성 (this 참조 포함!)
        Stack.push("HelloCore.instanceHello", coreInstance)
		
        // 4. 메서드 실행
        val result = method.implementation(coreInstance)
        println(result)
		
        Stack.pop()
		
        println("\n✅ 성공! this.message에 접근할 수 있었습니다.")
    }

    private fun callCompanionMethod() {
        println("🔍 코드: HelloCore.staticHello()  // Companion 메서드 호출\n")
		
        val classInfo = MethodArea.getClass("HelloCore")!!
        val companion = classInfo.companionObject!!
        val method = companion.methods["staticHello"]!!
		
        println("⚙️  JVM 내부 동작 3:")
        println("   1. Method Area에서 'HelloCore' 클래스 찾기 ✅ 3")
        println("   2. Companion object 찾기 ✅")
        println("   3. Companion 싱글톤 인스턴스 = 객체@${companion.instance.objectId} ✅")
        println("   4. 'staticHello' 메서드 찾기 ✅")
		
        // 스택 프레임 생성 (this = Companion 인스턴스)
        Stack.push("HelloCore\$Companion.staticHello", companion.instance)
		
        // 메서드 실행
        val result = method.implementation(companion.instance)
        println(result)
		
        Stack.pop()
		
        println("\n✅ 성공! Companion 인스턴스는 클래스 로딩 시 이미 생성되어 있습니다.")
    }
}

// ============================================
// 실행
// ============================================

fun main() {
    JVMSimulator.start()
	
    println("\n" + "=".repeat(60))
    println("📊 핵심 정리")
    println("=".repeat(60))
    println(
        """
        
        ┌─────────────────────────────────────────────────────────┐
        │  왜 인스턴스 메서드는 객체 없이 호출할 수 없는가?        │
        ├─────────────────────────────────────────────────────────┤
        │                                                          │
        │  1. 인스턴스 메서드는 'this' 참조가 필수                 │
        │     → this.message 같은 인스턴스 변수에 접근해야 함      │
        │                                                          │
        │  2. 객체가 없으면 this가 null                            │
        │     → 어떤 객체의 변수를 읽어야 할지 알 수 없음          │
        │                                                          │
        │  3. JVM은 메서드 호출 시 this를 첫 번째 파라미터로 전달  │
        │     instanceHello(this: HelloCore)                      │
        │                                                          │
        ├─────────────────────────────────────────────────────────┤
        │  왜 Companion 메서드는 바로 호출 가능한가?               │
        ├─────────────────────────────────────────────────────────┤
        │                                                          │
        │  1. Companion 객체는 클래스 로딩 시 자동 생성 (싱글톤)   │
        │     → Method Area에 이미 존재                           │
        │                                                          │
        │  2. this = Companion 싱글톤 인스턴스                     │
        │     → 항상 유효한 this 참조가 있음                       │
        │                                                          │
        │  3. 객체 생성 없이도 호출 가능                           │
        │     HelloCore.staticHello()                             │
        │     → 실제로는 HelloCore.Companion.INSTANCE.staticHello()│
        │                                                          │
        └─────────────────────────────────────────────────────────┘
        
        """.trimIndent(),
    )
}
