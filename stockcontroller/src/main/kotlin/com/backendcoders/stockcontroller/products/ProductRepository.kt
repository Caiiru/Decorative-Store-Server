package com.backendcoders.stockcontroller.products

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository:JpaRepository<Product,Long> {


}