package com.backendcoders.stockcontroller.roles.controller

import com.backendcoders.stockcontroller.roles.RoleService
import com.backendcoders.stockcontroller.roles.controller.requests.roleRequest
import com.backendcoders.stockcontroller.roles.controller.responses.RoleResponse
import com.backendcoders.stockcontroller.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleController (val service: RoleService) {

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles")
    fun insert(@Valid @RequestBody role: roleRequest)=
        service.insert(role.toRole()).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles/{name}")
    fun getByName(@PathVariable name:String) = service.findByNameOrNull(name)?.
            let { ResponseEntity.ok(RoleResponse(it)) }?:let { ResponseEntity.notFound().build() }
    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = service.findByIdOrNull(id)?.
            let{ResponseEntity.ok(RoleResponse(it))} ?: let{ResponseEntity.notFound().build()}

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    fun listAll(@RequestParam sortDir: String?=null) =
        service.findAll(SortDir.findOrThrow(sortDir?:"ASC"))
            .map{ RoleResponse(it) }
            .let{ResponseEntity.ok(it)
            }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:Long):ResponseEntity<Void> =
        if(service.deleteById(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()
}