package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.roles.Role
import com.backendcoders.stockcontroller.security.UserToken

import com.backendcoders.stockcontroller.security.Jwt.Companion.createAuthentication


object Stubs{
    fun userStub(id:Long? =1,
                 email: String? = "user@email.com",
                 password:String = "Str@ng1234",
                 nome:String = "user",
                 roles:List<String> = listOf()
    ) = User(id =id, name=nome, password = password, email = email?:"$nome@email.com",
        roles = roles.mapIndexed{i, it ->Role(i.toLong(), it, "$it role")}
            .toMutableSet())

    fun adminStub() = userStub(
        id=1000,
        email = "admin@stockcontroller.com",
        password="administrator",
        nome = "Administrator",
        roles = listOf("ADMIN")
    )

    fun roleStub(
        id:Long?=1,
        name:String = "USER",
        description:String = "RoleDescription"
    ) = Role(id = id, name = name, description = description)

    fun authStub(
        user: User
    ) = createAuthentication(UserToken(user))


}