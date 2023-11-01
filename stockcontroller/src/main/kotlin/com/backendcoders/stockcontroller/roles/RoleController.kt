package com.backendcoders.stockcontroller.roles

import com.backendcoders.stockcontroller.roles.requests.roleRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/roles")
class RoleController (val service: RoleService) {

    @PostMapping("/roles")
    fun insert(@Valid @RequestBody role: roleRequest)=
        service.insert(role.toRole()).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @GetMapping("/{name}")
    fun getByName(@PathVariable name:String) = service.findByNameOrNull(name)?.
            let { ResponseEntity.ok(it) }?:let { ResponseEntity.notFound().build() }


}