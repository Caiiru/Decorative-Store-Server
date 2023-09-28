package com.backendcoders.stockcontroller.users

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {

    @PostMapping("/users")
    fun insert (@RequestBody user: User) =
        service.insert(user)

    @GetMapping("/users")
    fun listAll() =
        service.findAll().map { it }
}