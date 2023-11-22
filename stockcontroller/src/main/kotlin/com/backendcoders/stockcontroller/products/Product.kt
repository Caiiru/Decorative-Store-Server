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

    @Column(unique = true, nullable = false)
    var name:String,
    @Column(nullable = false)
    var description:String? = null,
    @Column(nullable = false)
    var quantity:Int = 1,



    )