package com.backendcoders.stockcontroller.users.controller.requests

import jakarta.validation.constraints.NotBlank

data class PatchUserRequest(
    @field:NotBlank
    val name:String?
)
