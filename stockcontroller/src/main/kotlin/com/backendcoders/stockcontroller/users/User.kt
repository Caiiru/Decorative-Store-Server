package com.backendcoders.stockcontroller.users

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.Transient

@Entity
@Table(name = "tblUser")
class User (
    @Id @GeneratedValue
    var id: Long? = null,


    @Column(unique = true)
    var email: String,


    var password: String,
    var name: String

)