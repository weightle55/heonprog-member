package com.heonprog.member.entity

import jakarta.persistence.*


@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(length = 100, nullable = false, unique = true)
    val userId: String,
    @Column(length = 100, nullable = false, unique = true)
    val email: String,
    @Column(length = 300, nullable = false)
    val password: String
)