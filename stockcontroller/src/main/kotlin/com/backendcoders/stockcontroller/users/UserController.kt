package com.backendcoders.stockcontroller.users

import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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



    @GetMapping("/{id}")
    fun GetByID(@PathVariable id:Long) =
        service.findByIdOrNull(id)?.let{ it }
            ?:let { log.info("User with id:{} dont exist", id) }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:Long) =
        service.deleteById(id)?.let {
            it
        }

    companion object{
        val log = LoggerFactory.getLogger(UserController::class.java)
    }
}