package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.roles.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.Transient
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "tblUser")
class User (
    @Id @GeneratedValue
    var id: Long? = null,

    var name: String = "",
    @Column(unique = true)
    var email: String = "",


    var password: String = "",



    @ManyToMany
    @JoinTable(
        name="UserRole",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idRole")]
        )
    val roles: MutableSet<Role> = mutableSetOf()
)
{

    @get:JsonIgnore
    @get:Transient
    val isAdmin:Boolean get() = roles.any {it.name == "ADMIN"}
}