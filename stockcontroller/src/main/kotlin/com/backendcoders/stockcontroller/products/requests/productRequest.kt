package com.backendcoders.stockcontroller.products.requests

import com.backendcoders.stockcontroller.products.Product
import jakarta.validation.constraints.NotBlank

data class productRequest (
    @field:NotBlank
    val name:String,

    @field:NotBlank
    val qtd:Int,

    val description:String?
){
    fun toProduct() = Product(
        name = name!!,
        qtd = qtd!!,
        description = description
    )
}