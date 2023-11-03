package com.backendcoders.stockcontroller.roles

import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.users.SortDir
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoleService (val repository:RoleRepository){

    fun findByNameOrNull(name:String?):Role?{
        if(name.isNullOrBlank()) return null
        var roles = repository.findAll()
        for(r:Role in roles){
            log.info(r.name)
            if(r.name == name.uppercase()){
                return r
            }
        }

        return null
    }

    fun findByIdOrNull(id:Long):Role?{
        return repository.findByIdOrNull(id)
    }


    fun insert(role: Role):Role{
        val findRole = findByNameOrNull(role.name)
        if(findRole != null){
            throw BadRequestException("Role Already Exists!").also { log.info("Role Already Exists {}" , role.name) }
        }
        else{
            var newRole = role;
            newRole.name = role.name.uppercase()
            return repository.save(newRole).also { log.info("Role {} created: {}",role.id,role.name) }
        }
    }

    fun findAll(dir:SortDir = SortDir.ASC):List<Role> =
        when(dir){
            SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
            SortDir.DESC -> repository.findAll(Sort.by("name").descending())
        }



    fun deleteById(id:Long):Boolean{
        val role = findByIdOrNull(id)?:return false.also { throw NotFoundException(id) }
        log.info("Role (id: {}) {} deleted", role.id,role.name)
        repository.delete(role)
        return true
    }
    companion object{
        private val log = LoggerFactory.getLogger(RoleService::class.java)
    }

}