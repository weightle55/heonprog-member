package com.heonprog.member.model

data class JwtToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)