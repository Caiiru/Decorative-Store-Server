package com.backendcoders.stockcontroller.roles.requests

import com.backendcoders.stockcontroller.roles.Role
import jakarta.validation.constraints.NotBlank

class roleRequest (
    @field:NotBlank
    val name:String,

    val description:String

){
    fun toRole() = Role(
        name = name!!,
        description = description!!
    )
}