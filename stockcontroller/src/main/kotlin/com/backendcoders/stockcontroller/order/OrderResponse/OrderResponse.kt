package com.backendcoders.stockcontroller.order.OrderResponse

import com.backendcoders.stockcontroller.order.Order
import com.backendcoders.stockcontroller.order.OrderProduct.OrderProduct

data class OrderResponse (
    val id:Long,
    val userID:Long,
    val orderProduct: OrderProduct
){
}