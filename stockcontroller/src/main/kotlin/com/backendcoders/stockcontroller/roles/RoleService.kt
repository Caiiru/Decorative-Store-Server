package com.backendcoders.stockcontroller.roles

import com.backendcoders.stockcontroller.exception.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoleService (val repository:RoleRepository){

    fun findByNameOrNull(name:String?):Role?{
        if(name.isNullOrBlank()) return null
        var roles = repository.findAll()
        for(r:Role in roles){
            if(r.name == name){
                return r
            }
        }

        return null
    }


    fun insert(role: Role):Role{
        val findRole = findByNameOrNull(role.name)
        if(findRole != null){
            throw BadRequestException("Role Already Exists!").also { log.info("Role Already Exists {}" , role.name) }
        }
        else{
            return repository.save(role).also { log.info("Role {} created: {}",role.id,role.name) }
        }
    }


    companion object{
        private val log = LoggerFactory.getLogger(RoleService::class.java)
    }

}