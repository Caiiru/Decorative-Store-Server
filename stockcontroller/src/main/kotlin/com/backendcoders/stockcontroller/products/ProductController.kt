package com.backendcoders.stockcontroller.products

import com.backendcoders.stockcontroller.exception.ForbiddenException
import com.backendcoders.stockcontroller.products.requests.productRequest
import com.backendcoders.stockcontroller.security.UserToken
import com.backendcoders.stockcontroller.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
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
@RequestMapping("/products")
class ProductController(val service: ProductService) {

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @PostMapping("/products")
    fun insert(@RequestBody product:productRequest) = service.insert(product.toProduct())
        .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = service.findByIdOrNull(id)?.
        let { ResponseEntity.ok(it) }?:let { ResponseEntity.notFound().build() }


    @GetMapping("findByName/{name}")
    fun getByName(@PathVariable name:String) = service.findByName(name)?.
        let { ResponseEntity.ok(it) }?: let { ResponseEntity.notFound().build() }


    @GetMapping("/products")
    fun listAll(@RequestParam sortDir:String?=null) =
        service.findAll(SortDir.findOrThrow(sortDir?:"ASC")).map { it }.let { ResponseEntity.ok(it) }


    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    fun update(@PathVariable id:Long, product: productRequest):ResponseEntity<Product>{
        return service.update(id,product.toProduct())?.
            let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @DeleteMapping("/deleteById/{id}")
    fun deleteById(@PathVariable id:Long): ResponseEntity<Boolean> =
        if(service.delete(id) == true) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()


    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @DeleteMapping("/deleteByName/{name}")
    fun deleteByName(@PathVariable name:String):ResponseEntity<Void> =
        if(service.deleteByName(name)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()



}
