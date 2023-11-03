package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.roles.RoleRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository
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
        log.info("User {} deleted", user.id)
        repository.delete(user)
        return true
    }
    fun update(id: Long, user:User):User?{
        val newUser = findByIdOrThrow(id)
        if(user == newUser) return null

        newUser.name = user.name
        newUser.email = user.email
        newUser.password = user.password;

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
    fun findByRole(role:String):List<User> = repository.findByRole(role)

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}