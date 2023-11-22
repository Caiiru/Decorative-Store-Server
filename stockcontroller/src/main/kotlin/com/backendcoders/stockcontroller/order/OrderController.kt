package com.backendcoders.stockcontroller.order

import com.backendcoders.stockcontroller.order.OrderProduct.OrderProduct
import com.backendcoders.stockcontroller.order.OrderResponse.OrderResponse
import com.backendcoders.stockcontroller.order.requests.OrderRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController (val service:OrderService){

    @SecurityRequirement(name="StockController")
    @PreAuthorize("permitAll()")
    @PostMapping("/addOrder")
    fun addOrder(@Valid @RequestBody order: OrderRequest){
        service.insert(order).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    fun listAllOrders() = service.listAll().map { it }.let { ResponseEntity.ok(it) }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/productorders")
    fun listAllProductOrders() = service.listAllProductOrders().map { it }.let { ResponseEntity.ok(it) }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id:Long) = service.findByIdOrNull(id)?.
            let { ResponseEntity.ok(it) }?:let { ResponseEntity.notFound().build() }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/productorders/{id}")
    fun updateProductOrder(@PathVariable id:Long, orderProduct: OrderProduct): ResponseEntity<OrderProduct> {
        return service.productOrderUpdate(id,orderProduct)?.let { ResponseEntity.ok(it) }
            ?:ResponseEntity.noContent().build()
    }

    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id:Long, order: Order): ResponseEntity<Order> {
        return service.orderUpdate(id,order)?.let { ResponseEntity.ok(it) }
            ?:ResponseEntity.noContent().build()
    }


    @SecurityRequirement(name="StockController")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("productorders/delete/{id}")
    fun deleteProductOrderByID(@PathVariable id:Long, orderProduct: OrderProduct): ResponseEntity<OrderProduct> {
        return service.productOrderUpdate(id,orderProduct)?.let { ResponseEntity.ok(it) }
            ?:ResponseEntity.noContent().build()
    }



}