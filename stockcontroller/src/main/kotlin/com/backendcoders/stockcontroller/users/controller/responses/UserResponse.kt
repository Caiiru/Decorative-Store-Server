package com.backendcoders.stockcontroller.users.controller.responses

import com.backendcoders.stockcontroller.users.User

data class UserResponse (
    val id: Long,
    val email:String,
    val name:String,
){
    constructor(user: User) : this(user.id!!,user.email,user.name)
}