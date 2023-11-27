package com.heonprog.member.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created on 2023-11-28
 *
 * @author weightle55
 */

@Controller
class TestController {

    @RequestMapping("/test")
    fun testPage():String {
        return "html/test"
    }
}