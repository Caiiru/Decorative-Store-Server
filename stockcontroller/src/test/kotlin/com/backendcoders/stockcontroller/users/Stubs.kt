package com.backendcoders.stockcontroller.users

object Stubs{
    fun userStub(id:Long? =1,
                 email: String? = "user@email.com",
                 password:String = "Str@ng1234",
                 nome:String = "user"
    ) = User(id =id, name=nome, password = password, email = email!!)
}