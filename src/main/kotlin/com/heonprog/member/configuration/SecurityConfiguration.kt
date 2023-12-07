package com.heonprog.member.configuration

import jakarta.servlet.DispatcherType
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

/**
 * Created on 2023-11-26
 *
 * @author weightle55
 */

@EnableWebSecurity
@Configuration
class SecurityConfiguration {

    @Bean
    fun configureHttpSecurity(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf {
                it.disable()
                it.ignoringRequestMatchers("/h2-console/**")
            }
            .cors {
                it.disable()
            }
            .authorizeHttpRequests {
                it.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                it.requestMatchers("/node_modules/**", "/css/**", "/h2-console/**").permitAll()
                it.anyRequest().authenticated()
            }
            .formLogin {
                it.defaultSuccessUrl("/login/success", true).permitAll()
            }
            .headers {
                it.frameOptions { frameOptionsConfig ->
                    frameOptionsConfig.disable()
                }
            }
            .logout(Customizer.withDefaults())
        return httpSecurity.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(User.builder().username("testUser").password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("qwe123")).roles("USER").build())
    }
}