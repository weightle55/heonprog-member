package com.heonprog.member.configuration.jwt

import com.heonprog.member.model.JwtToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Slf4j
@Component
class JwtTokenProvider(
    val AUTHORITIES_KEY: String = "authorization",
    val BEARER_TYPE: String = "bearer",
    val ACCESS_EXPIRED: Long = 1000 * 60 * 30, //30분
    val REFRESH_EXPIRED: Long = 1000 * 60 * 30 * 7 // 7일
) {

    lateinit var secretKey : Key
    @Value("\${jwt.secret}")
    lateinit var secret: String
    fun init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret))
    }

    fun generateToken(authentication: Authentication): JwtToken {
        // 권한 획득
        val authorities = authentication.authorities.joinToString(",") { it.authority }

        // accessToken 생성
        val expiredTime = Date( Date().time + ACCESS_EXPIRED)
        val accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(expiredTime)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()

        // refreshToken
        val refreshToken = Jwts.builder()
            .setExpiration(Date(Date().time + REFRESH_EXPIRED))
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()

        return JwtToken(BEARER_TYPE, accessToken, refreshToken);
    }

    // JWT 토큰 복호화 및 정보 추출
    fun getAuthentication(accessToken: String): Authentication {
        // 토큰 복호화 : JWT 의 body
        val claims = parseClaim(accessToken)
        if (!claims.containsKey(AUTHORITIES_KEY)) {
            throw RuntimeException("권한 정보가 없는 토큰")
        }

        // claims 에서 권한 정보 가져오기
        val authorities = claims.get(AUTHORITIES_KEY).toString().split(",").map {
            SimpleGrantedAuthority(it)
        }.toList()

        // UserDetails 객체를 생성하여 Authentication return
        val principal: UserDetails = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateJwtToken(token: String) : Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (securityException: SecurityException) {
            false
        } catch (malformedJwtException : MalformedJwtException) {
            false
        } catch (e : ExpiredJwtException) {
            false
        }catch (e: UnsupportedJwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun parseClaim(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).body;
        } catch (e: ExpiredJwtException) {
            e.claims;
        }
    }

}