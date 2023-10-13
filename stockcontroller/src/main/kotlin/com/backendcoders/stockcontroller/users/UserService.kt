package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(val repository: UserRepository) {


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
    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
    fun update(id: Long, user:User):User?{
        val newUser = findByIdOrThrow(id)
        if(user == newUser) return null

        newUser.name = user.name
        newUser.email = user.email
        newUser.password = user.password;

        return repository.save(newUser)

    }


}