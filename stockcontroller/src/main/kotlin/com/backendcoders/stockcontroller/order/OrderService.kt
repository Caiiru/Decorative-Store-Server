package com.backendcoders.stockcontroller.order

import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.order.OrderProduct.OrderProduct
import com.backendcoders.stockcontroller.order.requests.OrderRequest
import com.backendcoders.stockcontroller.products.Product
import com.backendcoders.stockcontroller.products.ProductRepository
import com.backendcoders.stockcontroller.users.User
import com.backendcoders.stockcontroller.users.UserRepository
import com.backendcoders.stockcontroller.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Logger
import kotlin.jvm.optionals.getOrNull

@Service
class OrderService (
    val orderRepository: OrderRepository,
    val orderProductRepository: OrderProductRepository,
    val userRepository: UserRepository,
    val productRepository: ProductRepository
    ) {

    fun insert(order:OrderRequest){
        //Primeiro verificar o usuario
        val user = userRepository.findByIdOrNull(order.userID) ?: throw NotFoundException("User not found");
        //Pegar a lista de id dos produtos
        val products = order.products
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val currentDate = sdf.format(Date())
        val orderSave = Order(userID = order.userID, date = currentDate)
        for((id,quantidade) in products) {
            val product = productRepository.findByIdOrNull(id)?: throw BadRequestException("Product id not found")
            if(product.quantity < quantidade){
                throw BadRequestException("Product $id dont have quantity enough in stock")
                break;
            }
        }
        orderRepository.save(orderSave)
        for((id,quantidade) in products){
            val product = productRepository.findByIdOrNull(id)?: throw BadRequestException("Product id not found")
            val orderProductSave:OrderProduct = OrderProduct(ProductId = id, OrderId = orderSave.id, quantity = quantidade)
            orderProductRepository.save(orderProductSave)
            product.quantity -= orderProductSave.quantity!!
        }
        log.info("Order created, User: {} Order: {}", orderSave.userID, orderSave.id)
    }

    fun listAll():List<Order> = orderRepository.findAll()
    fun listAllProductOrders():List<OrderProduct> = orderProductRepository.findAll()

    fun findByIdOrNull(id:Long):Order?{
        return orderRepository.findById(id).getOrNull()
    }
    fun orderProductFindByIdOrNull(id:Long):OrderProduct?{
        return orderProductRepository.findById(id).getOrNull()
    }

    fun orderUpdate(id: Long, order: Order):Order?{
        val newOrder = findByIdOrNull(id)?: throw NotFoundException("Order not Found")
        if(order==newOrder) return null

        newOrder.userID = order.userID
        newOrder.date = order.date


        return orderRepository.save(newOrder)
    }

    fun productOrderUpdate(id:Long, orderProduct: OrderProduct):OrderProduct?
    {
        val newOrderProduct = orderProductFindByIdOrNull(id)?: throw NotFoundException("Order Product not found")
        if(orderProduct == newOrderProduct) return null

        newOrderProduct.ProductId = orderProduct.ProductId
        newOrderProduct.OrderId = orderProduct.OrderId
        newOrderProduct.quantity = orderProduct.quantity

        return orderProductRepository.save(newOrderProduct)
    }

    fun deleteProductOrderByID(id:Long):Boolean{
        val orderProduct = orderProductFindByIdOrNull(id)?:return false.also { throw NotFoundException(id) }
        log.info("Product id: {} deleted", orderProduct.id)
        orderProductRepository.delete(orderProduct)
        return true
    }

    companion object{
        private val log = LoggerFactory.getLogger(OrderService::class.java)
    }
}