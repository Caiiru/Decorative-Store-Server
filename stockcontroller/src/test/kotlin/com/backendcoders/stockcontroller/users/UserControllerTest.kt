package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.users.controller.requests.CreateUserRequest
import com.backendcoders.stockcontroller.users.controller.responses.UserResponse
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import com.backendcoders.stockcontroller.users.Stubs.userStub
import com.backendcoders.stockcontroller.users.controller.UserController
import org.junit.jupiter.api.assertThrows

class UserControllerTest {

    private val serviceMock = mockk<UserService>()
    private val controller = UserController(serviceMock)

    @BeforeEach
    fun setup(){
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp(){
        checkUnnecessaryStub(serviceMock)
    }

    @Test
    fun `insert must return a code when insert`(){
        val user = Stubs.userStub()
        val request = CreateUserRequest(user.email,user.password,user.name)

        every { serviceMock.insert(any()) } returns user
        with(controller.insert(request)){
            statusCode shouldBe HttpStatus.CREATED
            body shouldBe UserResponse(user)
        }

    }

    @Test
    fun `listAll must return all users with the given paramater`(){
        val listUser = listOf(userStub(1, nome = "Ana"), userStub(2, nome = "Beringela"), userStub(3, nome = "Jorge"))

        every { serviceMock.findAll(SortDir.DESC) } returns listUser
        with(controller.listAll("DESC")) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe listUser.map { UserResponse(it) }
        }
    }

    @Test
    fun `listAll must have ASC by default`(){
        val listUser = listOf(userStub(1, nome = "Ana"), userStub(2, nome = "Beringela"), userStub(3, nome = "Jorge"))
        every { serviceMock.findAll(SortDir.ASC) } returns listUser
        with(controller.listAll(null)) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe listUser.map { UserResponse(it) }
        }
    }

    @Test
    fun `getById must return the user passed by id`(){
        val user = userStub(id=6)
        every{
            serviceMock.findByIdOrNull(6)
        } returns user
        with(controller.getByID(6)){
            statusCode shouldBe HttpStatus.OK
            body shouldBe UserResponse(user)
        }
    }

    @Test
    fun `getById must return notfound if doesnt find user`(){
        val user = userStub(id=1)
        every{
            serviceMock.findByIdOrNull(1)
        } returns null
        with(controller.getByID(1)){
            statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    @Test
    fun `deleteById must return ok if user was deleted`(){

        every{
            serviceMock.delete(1)
        } returns true

        with(controller.deleteById(1)){
            statusCode shouldBe HttpStatus.OK
        }
    }

    @Test
    fun `deleteById must return notFound if user doenst exists`(){
        every{
            serviceMock.delete(1)
        } returns false

        with(controller.deleteById(1)){
            statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    @Test
    fun `update must return NO CONTENT if the service returns null`(){
        val user= userStub(id=1)
        every { serviceMock.update(1,any()) } returns null
        with(controller.update(1, CreateUserRequest(
            "email2@email.com",
            "SDWQ@!123",
            "Miguelito")
        )){
            statusCode shouldBe HttpStatus.NO_CONTENT
            body shouldBe null
        }
    }

    @Test
    fun `update must update if user is updating himself`(){
        val user = userStub(id = 3, nome = "Maiquin")
        every { serviceMock.update(3,any()) }returns user
        with(controller.update(3, CreateUserRequest(
            "email2@email.com",
            "SDWQ@!123",
            "Miguelito"
        )
        )){
            statusCode shouldBe HttpStatus.OK
            body shouldBe UserResponse(user)
        }
    }

    @Test
    fun `update must throw NOT FOUND EXCEPTION if the user doenst exists`(){
        val user = userStub(1)
        every { serviceMock.update(1,any()) } throws NotFoundException()
        assertThrows<NotFoundException> {
            controller.update(1, CreateUserRequest(user.email,user.password,user.name))
        }
    }

}