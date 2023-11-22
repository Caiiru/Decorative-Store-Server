package com.backendcoders.stockcontroller

import com.backendcoders.stockcontroller.order.OrderProductRepository
import com.backendcoders.stockcontroller.order.OrderRepository
import com.backendcoders.stockcontroller.products.Product
import com.backendcoders.stockcontroller.products.ProductRepository
import com.backendcoders.stockcontroller.roles.Role
import com.backendcoders.stockcontroller.roles.RoleRepository
import com.backendcoders.stockcontroller.users.User
import com.backendcoders.stockcontroller.users.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val orderRepository: OrderRepository,
    val productRepository: ProductRepository,
    val orderProductRepository: OrderProductRepository,
    @Qualifier("defaultAdmin") val adminUser: User
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole =
                roleRepository.findByName("ADMIN")?:
                roleRepository.save(Role(name="ADMIN", description = "System Administrator"))
                    .also {
                        roleRepository.save(Role(name = "EMPLOYEE", description = "Employee User "))
                        roleRepository.save(Role(name = "CLIENT", description = "Client User"))
                        log.info("Admin, Employee and Client roles are created")
                    }
        if(userRepository.findByRole("ADMIN").isEmpty()){
            adminUser.roles.add(adminRole)
            userRepository.save(adminUser)
            log.info("First Administrator created")
        }

        if(productRepository.findAll().isEmpty()){
            val product = Product(
                id = 1,
                name = "Filamento Branco PLA",
                description = "Filamento para impressão 3D",
                quantity = 300)
            productRepository.save(product)
            val product2 = Product(
                id = 2,
                name = "Mesa Digitalizadora",
                description = "Mesa Digitalizadora para desenhos e ilustrações digitais",
                quantity = 15)
            productRepository.save(product2)
            val product3 = Product(
                id = 3,
                name = "Kit Gamer",
                description = "Kit Gamer pronto para uso. Incluso: Mouse, Teclado e Headset",
                quantity = 52)
            productRepository.save(product3)
        }

    }
    companion object{
        private val log = LoggerFactory.getLogger(Bootstrapper::class.java)
    }
}

