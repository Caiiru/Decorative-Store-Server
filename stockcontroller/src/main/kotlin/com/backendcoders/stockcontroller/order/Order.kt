package com.backendcoders.stockcontroller.order

import com.backendcoders.stockcontroller.products.Product
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.util.Date

@Entity
@Table(name="TblOrder")
data class Order (

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id:Long?=null,

    @NotBlank
    var userID:Long?=null,

    @NotBlank
    var date:String?=null
    )

