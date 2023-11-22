package com.backendcoders.stockcontroller.order.OrderProduct

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name="OrderProduct")
data class OrderProduct (
    @Id @GeneratedValue
    val id:Long?=null,

    @NotBlank
    var ProductId:Long?=null,

    @NotBlank
    var OrderId:Long?=null,

    @NotBlank
    var quantity:Int?=null,



)