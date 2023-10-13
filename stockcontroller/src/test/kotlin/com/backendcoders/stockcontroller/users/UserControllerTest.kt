package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.users.requests.CreateUserRequest
import com.backendcoders.stockcontroller.users.responses.UserResponse
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.apache.coyote.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import com.backendcoders.stockcontroller.users.Stubs
import com.backendcoders.stockcontroller.users.Stubs.userStub

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

}