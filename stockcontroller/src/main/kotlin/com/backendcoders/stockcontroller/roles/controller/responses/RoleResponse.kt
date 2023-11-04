package com.backendcoders.stockcontroller.roles.controller.responses

import com.backendcoders.stockcontroller.roles.Role

data class RoleResponse(
    val name:String,
    val description:String
){
    constructor(role: Role): this(name=role.name, description=role.description)
}
