package com.backendcoders.stockcontroller.users

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(val repository: UserRepository) {


    fun insert(user: User):User {
        return repository.save(user)
            .also { log.info("User {} inserted ", user.id) }

    }

    fun findAll():List<User> = repository.findAll()


    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }

}