package com.backendcoders.stockcontroller.products

import com.backendcoders.stockcontroller.products.requests.productRequest
import com.backendcoders.stockcontroller.users.SortDir
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
@RequestMapping("/products")
class ProductController(val service: ProductService) {

    @PostMapping("/products")
    fun insert(@RequestBody product:productRequest) = service.insert(product.toProduct())
        .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long) = service.findByIdOrNull(id)?.
        let { ResponseEntity.ok(it) }?:let { ResponseEntity.notFound().build() }

    @GetMapping("findByName/{name}")
    fun getByName(@PathVariable name:String) = service.findByName(name)?.
        let { ResponseEntity.ok(it) }?: let { ResponseEntity.notFound().build() }

    @GetMapping("/products")
    fun listAll(@RequestParam sortDir:String?=null) =
        service.findAll(SortDir.findOrThrow(sortDir?:"ASC")).map { it }.let { ResponseEntity.ok(it) }

    @PutMapping("/{id}")
    fun update(@PathVariable id:Long, product: productRequest):ResponseEntity<Product>{
        return service.update(id,product.toProduct())?.
            let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:Long):ResponseEntity<Void> =
        if(service.delete(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()

    @DeleteMapping("/{name}")
    fun deleteByname(@PathVariable name:String):ResponseEntity<Void> =
        if(service.deleteByName(name)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()



}
