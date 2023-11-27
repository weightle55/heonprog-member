package com.heonprog.member.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Created on 2023-11-28
 *
 * @author weightle55
 */
@Controller
class LoginController {

    @GetMapping("/login/success")
    fun loginSuccess(): String {
        return "/html/login/success"
    }
}