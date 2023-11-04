package com.backendcoders.stockcontroller.roles.controller

import com.backendcoders.stockcontroller.roles.RoleService
import com.backendcoders.stockcontroller.roles.controller.requests.roleRequest
import com.backendcoders.stockcontroller.roles.controller.responses.RoleResponse
import com.backendcoders.stockcontroller.users.SortDir
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleController (val service: RoleService) {

    @PostMapping("/roles")
    fun insert(@Valid @RequestBody role: roleRequest)=
        service.insert(role.toRole()).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @GetMapping("/roles/{name}")
    fun getByName(@PathVariable name:String) = service.findByNameOrNull(name)?.
            let { ResponseEntity.ok(RoleResponse(it)) }?:let { ResponseEntity.notFound().build() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = service.findByIdOrNull(id)?.
            let{ResponseEntity.ok(RoleResponse(it))} ?: let{ResponseEntity.notFound().build()}

    @GetMapping("/roles")
    fun listAll(@RequestParam sortDir: String?=null) =
        service.findAll(SortDir.findOrThrow(sortDir?:"ASC"))
            .map{ RoleResponse(it) }
            .let{ResponseEntity.ok(it)
            }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:Long):ResponseEntity<Void> =
        if(service.deleteById(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()
}