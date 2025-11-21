package com.ms.multi.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    /**
     * 등록자
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: String? = null
        protected set

    /**
     * 등록 일시
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime? = null
        protected set

    /**
     * 수정자
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: String? = null
        protected set

    /**
     * 수정 일시
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime? = null
        protected set

    /**
     * 버전
     */
    @Version
    var version: Int? = null
        protected set
}
