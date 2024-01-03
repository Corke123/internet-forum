package org.unibl.etf.sni.forumservice.user

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/users/me")
    fun me(authentication: Authentication): User {
        println(authentication.authorities)
        return User(authentication.name)
    }

    data class User(val username: String)
}