package com.backendcoders.stockcontroller.products

import com.backendcoders.stockcontroller.products.requests.productRequest
import com.backendcoders.stockcontroller.users.SortDir
import com.backendcoders.stockcontroller.users.Stubs
import com.backendcoders.stockcontroller.users.controller.requests.CreateUserRequest
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ProductControllerTest {
    private val serviceMock = mockk<ProductService>()
    private val controller = ProductController(serviceMock)

    @BeforeEach
    fun setup(){
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp(){
        checkUnnecessaryStub(serviceMock)
    }

    @Test
    fun `Insert Product must return a code when product is inserted`(){
        val product = productStubs.productStub()
        val request = productRequest(product.name,product.quantity,product.description)

        every{
            serviceMock.insert(any())
        } returns product
        with(controller.insert(request)){
            statusCode shouldBe HttpStatus.CREATED
            body shouldBe product
        }
    }

    @Test
    fun `listAll must return all products with the given parameter`(){
        val listProduct = listOf(productStubs.productStub(1,name="Filamento"),productStubs.productStub(2,"Mesa Digitalizadora"))

        every{
            serviceMock.findAll(SortDir.DESC)
        } returns listProduct

        with(controller.listAll("DESC")){
            statusCode shouldBe  HttpStatus.OK
            body shouldBe  listProduct.map { it }
        }
    }

    @Test
    fun `listAll must have ASC by default`(){
        val listProduct = listOf(productStubs.productStub(1,name="Filamento"),productStubs.productStub(2,"Mesa Digitalizadora"))
        every{serviceMock.findAll(SortDir.ASC)} returns listProduct
        with(controller.listAll(null)){
            statusCode shouldBe HttpStatus.OK
            body shouldBe listProduct.map { it }
        }
    }

    @Test
    fun `getByID must return the product passed by id`(){
        val product = productStubs.productStub(id=87)
        every { serviceMock.findByIdOrNull(any()) } returns product
        with (controller.getById(1)){
            statusCode shouldBe HttpStatus.OK
            body shouldBe product
        }
    }

    @Test
    fun `getById must return NotFound if product doenst exist`(){

        every { serviceMock.findByIdOrNull(any()) } returns null
        with(controller.getById(1)){
            statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    @Test
    fun `deleteById must return ok if product was deleted`(){
        every{
            serviceMock.delete(any())
        } returns true

        with (controller.deleteById(1)){
            statusCode shouldBe  HttpStatus.OK
        }
    }
    @Test
    fun `deleteById must return notFound if product doesnt exists`(){
        every{
            serviceMock.delete(any())
        } returns false

        with(controller.deleteById(1)){
            statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    @Test
    fun `deleteByName must return ok if product was deleted`() {
        every {
            serviceMock.deleteByName(any())
        } returns true

        with(controller.deleteByName("name")){
            statusCode shouldBe HttpStatus.OK
        }
    }
    @Test
    fun `deleteByName must return notFound if product doesnt exists`(){
        every{
            serviceMock.deleteByName(any())
        } returns false

        with(controller.deleteByName("name")){
            statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    @Test
    fun `update must return NO CONTENT if the service retuns null`(){
        val product = productStubs.productStub(id=5)
        every { serviceMock.update(5,any()) } returns null
        with(controller.update(product.id!!, productRequest(product.name,product.quantity,product.description))){
            statusCode shouldBe HttpStatus.NO_CONTENT
            body shouldBe null
        }
    }
}