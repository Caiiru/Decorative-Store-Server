package com.backendcoders.stockcontroller.roles.controller.requests

import com.backendcoders.stockcontroller.roles.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class roleRequest (
    @field:NotBlank
    @Pattern(regexp = "^[A-Z][A-Z0-9]+\$")
    val name:String,

    @NotBlank
    val description:String

){
    fun toRole() = Role(
        name = name!!,
        description = description!!
    )
}