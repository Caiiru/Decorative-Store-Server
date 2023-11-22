package com.backendcoders.stockcontroller.products

object productStubs {
    fun productStub(id:Long? =1,
                   name:String = "productStub",
                    description:String? = "Normal product",
                    quantity:Int =1) = Product(id=id,name=name,description = description, quantity = quantity)
}