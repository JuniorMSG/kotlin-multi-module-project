package com.ms.multi.domain.user

import com.ms.multi.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Suppress("LongParameterList")
@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var purchaseAmount: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var grade: UserGrade = UserGrade.BRONZE,

    @Column(nullable = false)
    var markedForUpdate: Boolean = false,


): BaseEntity()


