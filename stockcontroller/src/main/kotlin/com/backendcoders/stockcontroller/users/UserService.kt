package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.roles.RoleRepository
import com.backendcoders.stockcontroller.security.Jwt
import com.backendcoders.stockcontroller.users.controller.responses.LoginResponse
import com.backendcoders.stockcontroller.users.controller.responses.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository,
    val jwt: Jwt
    ) {


    fun insert(user: User):User {
        if(repository.findByEmail(user.email) == null) {
            return repository.save(user)
                .also { log.info("User {} inserted ", user.id) }
        }
        else{
            log.info("User Already Exists")
           throw BadRequestException("User Already Exists!")
        }
    }

    fun findAll(dir: SortDir = SortDir.ASC):List<User> = when(dir){
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id:Long) = repository.findById(id).getOrNull()
    private fun findByIdOrThrow(id:Long) = findByIdOrNull(id)?: throw NotFoundException(id)

    public fun findByName(name:String):User?{
        if(name.isNullOrBlank()) return null
        var users = repository.findAll()
        for(u : User in users){
            if(u.name == name){
                return u
            }
        }
        return null
    }
    private fun findByEmailOrNull(email:String):String?{
        var users = repository.findAll()
        for (u: User in users){
            if(u.email == email)
                return email
        }
        return null
    }
    fun delete(id:Long):Boolean{
        val user = findByIdOrNull(id)?:return false.also { throw NotFoundException(id) }
        if(user.roles.any(){it.name == "ADMIN"}){
            val count = repository.findByRole("ADMIN").size
            if (count == 1 ) throw BadRequestException("Cannot delete the last system administrator!")
        }
        repository.delete(user)
        log.info("User {} deleted", user.id)
        return true
    }
    fun update(id: Long, name:String):User?{
        val newUser = findByIdOrThrow(id)
        if(newUser.name == name) return null
        newUser.name = name

        return repository.save(newUser)
    }

    fun addRole(id:Long, roleName:String):Boolean{
        val user = findByIdOrThrow(id)
        if(user.roles.any{it.name == roleName}) return false

        val role = roleRepository.findByName(roleName)?:
            throw BadRequestException("Invalid role: $roleName").also {
                log.info("Invalid role: $roleName")
            }

        user.roles.add(role)
        repository.save(user)
        log.info("Granted role {} to user {}", role.name, user.id)
        return true
    }

    fun login(email:String, password:String): LoginResponse?{
        val user = repository.findByEmail(email) ?: return null
        if(user.password != password) return null

        log.info("User logged in. id={}, name={}", user.id,user.name)
        return LoginResponse(token = jwt.createToken(user), user = UserResponse(user))
    }
    fun findByRole(role:String):List<User> = repository.findByRole(role)

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}