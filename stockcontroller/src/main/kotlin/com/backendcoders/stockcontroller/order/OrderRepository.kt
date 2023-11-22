package com.backendcoders.stockcontroller.order

import com.backendcoders.stockcontroller.order.OrderProduct.OrderProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository:JpaRepository<Order,Long> {
}