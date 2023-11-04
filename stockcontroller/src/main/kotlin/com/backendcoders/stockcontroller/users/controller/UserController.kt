package com.backendcoders.stockcontroller.users.controller

import com.backendcoders.stockcontroller.exception.ForbiddenException
import com.backendcoders.stockcontroller.security.UserToken
import com.backendcoders.stockcontroller.users.SortDir
import com.backendcoders.stockcontroller.users.UserService
import com.backendcoders.stockcontroller.users.controller.requests.CreateUserRequest
import com.backendcoders.stockcontroller.users.controller.requests.LoginRequest
import com.backendcoders.stockcontroller.users.controller.requests.PatchUserRequest
import com.backendcoders.stockcontroller.users.controller.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {

    @PostMapping("/users")
    fun insert (@Valid @RequestBody user: CreateUserRequest) =
        UserResponse(service.insert(user.toUser())).
                let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @GetMapping()
    fun listAll(@RequestParam sortDir:String?=null, @RequestParam role:String? = null) =
        if(role == null){
            service.findAll(SortDir.findOrThrow(sortDir?:"ASC"))
        }
        else {
            service.findByRole(role.uppercase())
        }.map { UserResponse(it) }.let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun getByID(@PathVariable id:Long) =
        service.findByIdOrNull(id)?.let{ResponseEntity.ok(UserResponse(it)) }
            ?:let { ResponseEntity.notFound().build() }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:Long):ResponseEntity<Void> =
        if(service.delete(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()

    @SecurityRequirement(name="StockController")
    @PreAuthorize("permitAll()")
    @PatchMapping("/{id}")
    fun update(@PathVariable id:Long,@Valid @RequestBody request:PatchUserRequest,auth:Authentication):ResponseEntity<UserResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if(token.id != id && !token.isAdmin) throw ForbiddenException()

        return service.update(id,request.name!!)
            ?.let{ ResponseEntity.ok(UserResponse(it)) }
            ?:ResponseEntity.noContent().build()
    }
    @PutMapping("/{id}/roles/{role}")
    fun grant(@PathVariable id:Long, @PathVariable role:String):ResponseEntity<Void> =
        if(service.addRole(id,role.uppercase())){
            ResponseEntity.ok().build()
        }else{
            ResponseEntity.noContent().build()
        }

    @PostMapping("/login")
    fun login(@Valid @RequestBody login:LoginRequest)=
        service.login(login.email!!, login.password!!)
            ?.let { ResponseEntity.ok(it) }
            ?:ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

    companion object{
        val log = LoggerFactory.getLogger(UserController::class.java)
    }
}