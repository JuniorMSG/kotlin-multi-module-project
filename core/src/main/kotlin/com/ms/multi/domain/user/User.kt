package com.ms.multi.domain.user

import com.ms.multi.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_user_email", columnList = "email"),
    ],
)
class User(
    /**
     * 고유 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    /**
     * 이메일 (로그인 ID)
     * - unique 제약조건
     * - 인덱스 추가로 조회 성능 향상
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    val email: String,
    /**
     * 암호화된 비밀번호
     * - BCrypt 등으로 암호화 필수
     * - length = 255 (암호화된 문자열 길이 고려)
     */
    @Column(name = "password", nullable = false, length = 255)
    val password: String,
    /**
     * 사용자 이름
     */
    @Column(name = "name", nullable = false, length = 50)
    val name: String,
    /**
     * 계정 활성화 여부
     */
    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,
    /**
     * 사용자 역할 (ADMIN, USER 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    val role: UserRole = UserRole.USER,
    /**
     * 마지막 로그인 시간
     */
    @Column(name = "last_login_at")
    val lastLoginAt: LocalDateTime? = null,
    /**
     * 로그인 실패 횟수
     */
    @Column(name = "failed_login_attempts", nullable = false)
    val failedLoginAttempts: Int = 0,
    /**
     * 계정 잠금 여부
     */
    @Column(name = "is_locked", nullable = false)
    val isLocked: Boolean = false,
) : BaseEntity()

/**
 * 사용자 역할
 */
enum class UserRole {
    ADMIN,
    USER,
    GUEST,
}
