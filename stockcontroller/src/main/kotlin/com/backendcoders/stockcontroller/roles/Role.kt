package com.backendcoders.stockcontroller.roles

import com.backendcoders.stockcontroller.users.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Role (
    @Id @GeneratedValue
    val id:Long?=null,

    @Column(unique = true, nullable = false)
    var name:String,

    @Column(nullable = false)
    val description:String = "",

    @ManyToMany(mappedBy = "roles")
    val users:MutableSet<User> = mutableSetOf()
)