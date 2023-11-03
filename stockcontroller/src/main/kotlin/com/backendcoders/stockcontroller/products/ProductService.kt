package com.backendcoders.stockcontroller.products

import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.users.SortDir
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val repository: ProductRepository) {

    fun insert(product: Product):Product{
        val findProduct = findByName(product.name);
        if(findProduct != null){
            findProduct.qtd += product.qtd
            log.info("Adicionando mais uma unidade do produto {}", product.name)
            return repository.save(findProduct)

        }else{
            return repository.save(product).
                    also { log.info("Product {} inserted", product.name) }
        }
    }


    fun findByIdOrNull(id: Long):Product? {
        return repository.findById(id).getOrNull()
    }
    fun findByIdOrThrow(id: Long) = findByIdOrNull(id) ?: throw NotFoundException(id);
    fun findByName(name:String?):Product?{
        if(name.isNullOrBlank()) return null;
        var products = repository.findAll()
        for (p: Product in products){
            if(p.name == name){
                return p
            }
        }
        return null
    }

    fun findAll(dir:SortDir = SortDir.ASC):List<Product> =
        when(dir){
            SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
            SortDir.DESC -> repository.findAll(Sort.by("name").descending())

        }

    fun delete(id:Long):Boolean {
        val product = findByIdOrNull(id)?:return false.also { throw NotFoundException(id) }
        log.info("Product (id: {}) {} deleted", product.id,product.name )
        repository.delete(product)
        return true
    }
    fun deleteByName(name:String):Boolean{
        val product = findByName(name)?:return  false.also { throw NotFoundException(name) }
        log.info("Product (id: {}) {} deleted", product.id,product.name )
        repository.delete(product)
        return true
    }

    fun update(id:Long, product:Product):Product?{
        val newProduct = findByIdOrThrow(id)
        if(product == newProduct) return null

        newProduct.name = product.name
        newProduct.qtd = product.qtd
        newProduct.description = product.description

        return repository.save(newProduct)
    }


    companion object{
        private val log = LoggerFactory.getLogger(ProductService::class.java)
    }
}