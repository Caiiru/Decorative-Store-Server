package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.users.requests.CreateUserRequest
import com.backendcoders.stockcontroller.users.responses.UserResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {

    @PostMapping("/users")
    fun insert (@Valid @RequestBody user: CreateUserRequest) =
        UserResponse(service.insert(user.toUser())).
                let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @GetMapping("/users")
    fun listAll(@RequestParam sortDir:String?=null) =
        service.findAll(SortDir.findOrThrow(sortDir?:"ASC"))
            .map { UserResponse(it) }
            .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun getByID(@PathVariable id:Long) =
        service.findByIdOrNull(id)?.let{ResponseEntity.ok(UserResponse(it)) }
            ?:let { ResponseEntity.notFound().build() }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:Long) =
        service.delete(id)?.let{it}

    @PutMapping("/{id}")
    fun update(@PathVariable id:Long, user:CreateUserRequest)=
        service.update(id,user.toUser())

    companion object{
        val log = LoggerFactory.getLogger(UserController::class.java)
    }
}