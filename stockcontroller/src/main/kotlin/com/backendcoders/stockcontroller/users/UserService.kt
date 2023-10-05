package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(val repository: UserRepository) {


    fun insert(user: User):User {
        return repository.save(user)
            .also { log.info("User {} inserted ", user.id) }

    }

    fun findAll(dir: SortDir = SortDir.ASC):List<User> = when(dir){
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id:Long) = repository.findById(id).getOrNull()
    private fun findByIdOrThrow(id:Long) = findByIdOrNull(id)?: throw NotFoundException(id)

    fun deleteById(id:Long) = repository.deleteById(id)
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