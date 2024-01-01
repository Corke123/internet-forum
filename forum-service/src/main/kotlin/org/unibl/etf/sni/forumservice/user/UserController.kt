package org.unibl.etf.sni.forumservice.user

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/me")
    fun me(authentication: Authentication) = User(authentication.name)

    data class User(val username: String)
}