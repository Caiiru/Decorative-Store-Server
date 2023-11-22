package com.backendcoders.stockcontroller.order.requests

import jakarta.validation.constraints.NotBlank

data class OrderRequest (
    @NotBlank
    val userID:Long,

    @NotBlank
    val products:Map<Long,Int>
)