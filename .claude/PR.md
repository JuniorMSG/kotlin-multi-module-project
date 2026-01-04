# PR 작성 요청서

## 🎯 작업 요청

현재 브랜치의 변경사항을 분석하여 Pull Request 문서를 작성해주세요.

### 수행할 작업

1. **브랜치 및 Base 확인**
   ```bash
   git branch --show-current
   git log --oneline --graph --all --decorate | head -15
   ```

2. **변경사항 분석**
   ```bash
   git diff <base-branch>...HEAD --stat
   git diff <base-branch>...HEAD  # 주요 파일만 확인
   ```

3. **PR 문서 작성**
    - `.claude/PR/<브랜치명>.md` 파일 생성
    - 아래 가이드라인 준수

---

## 📋 작성 가이드라인

### 필수 포함 내용

1. **PR 유형** - 버그수정 / 새로운 기능 / 리팩토링
2. **Base 브랜치** - main 또는 feature 브랜치 명시 (feature인 경우 간략히 설명)
3. **작업 내용**
    - 이슈 트래커 번호 (Jira, GitHub Issues 등)
    - 주요 변경사항 항목별 정리 (핵심에 ⭐)
4. **변경 배경** - 왜 필요한지 2-3문장으로 간결하게
5. **리뷰 포인트**
    - 파일 경로와 라인 번호 포함
    - 중요 로직에 ⚠️ 표시
6. **체크리스트** - 테스트 / DDL / Breaking Change
7. **참고자료** - 핵심 코드 스니펫, 이슈 링크

### 중복 제거 원칙

- **주요 변경사항은 간단히, 코드는 참고자료에만 상세히**
- **배경 설명은 2-3문장, 기술 세부사항은 코드 주석으로**
- **커밋 히스토리, 통계 제외**

---

## ⚠️ 주의사항

1. **시간 표현 금지** - "2주 소요", "나중에" 등
2. **최종 상태만** - 중간 수정 과정 생략
3. **Base 브랜치 명확히** - feature 브랜치 base인 경우 이유 설명
4. **Co-authored-By 필수** - 마지막에 반드시 추가

---

## 📝 작성 예시

```markdown
## 설명

### PR 유형
**새로운 기능 추가**

### Base 브랜치
`feature/ISSUE-001` (신규 기능 브랜치)

### 작업 내용
ISSUE-001: 주문 조회 API 구현

#### 주요 변경사항
1. ⭐ **Order 도메인 모델 추가**
   - Order, OrderItem 엔티티 정의
   - OrderStatus enum 구현
2. **주문 조회 UseCase 구현**
   - ID 기반 단건 조회
   - 사용자별 목록 조회 (페이징)
3. **Repository 구현**
   - JPA Repository + QueryDSL 커스텀 쿼리

### 배경
사용자가 자신의 주문 내역을 확인할 수 있어야 함. 
기존에는 관리자 API만 존재하여 사용자용 조회 API 신규 개발 필요.

### 리뷰 포인트
1. ⚠️ **조회 로직 null 처리** - `OrderQueryService.kt:45-58`
   - Optional 대신 nullable 타입 사용, 적절성 검토 필요
2. **QueryDSL 페이징** - `OrderRepositoryImpl.kt:32-48`
   - offset 기반 페이징, 대용량 데이터 시 성능 고려 필요
3. **도메인 모델 설계** - `Order.kt:12-35`
   - 불변 객체로 설계, 상태 변경은 새 인스턴스 반환

## 체크리스트
- [x] 테스트코드 작성 및 통과
- [x] DDL 변경사항 없음
- [x] Breaking Change 없음
- [ ] API 문서 업데이트 필요

## 참고자료

### 핵심 코드

```kotlin
// OrderQueryService.kt:45-58
class OrderQueryService(
    private val orderRepository: OrderRepository
) {
    fun findById(orderId: Long): Order? {
        return orderRepository.findById(orderId)
    }
    
    fun findByUserId(userId: Long, pageable: Pageable): Page<Order> {
        return orderRepository.findByUserId(userId, pageable)
    }
}
```

```kotlin
// Order.kt:12-35
data class Order(
    val id: Long,
    val userId: Long,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val createdAt: LocalDateTime
) {
    fun cancel(): Order = copy(status = OrderStatus.CANCELLED)
    
    val totalAmount: BigDecimal
        get() = items.sumOf { it.price * it.quantity.toBigDecimal() }
}
```

### 관련 이슈
- [ISSUE-001](https://your-tracker.com/ISSUE-001)

---

Co-authored-By: Claude <noreply@anthropic.com>
```

---

## 🚀 실행

1. 위 명령어로 브랜치 및 변경사항 분석
2. 가이드라인에 따라 간결하게 작성
3. **마지막에 Co-authored-By 필수 추가**
