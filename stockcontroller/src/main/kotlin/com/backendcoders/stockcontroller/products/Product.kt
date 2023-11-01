package com.backendcoders.stockcontroller.products

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tblProduct")
class Product (
    @Id @GeneratedValue
    var id: Long? = null,
    var name:String,
    var description:String? = null,
    var qtd:Int = 1
)