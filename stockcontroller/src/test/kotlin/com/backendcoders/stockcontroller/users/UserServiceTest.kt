package com.backendcoders.stockcontroller.users
import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import com.backendcoders.stockcontroller.roles.Role
import com.backendcoders.stockcontroller.roles.RoleRepository
import com.backendcoders.stockcontroller.security.Jwt
import com.backendcoders.stockcontroller.users.Stubs.roleStub
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.backendcoders.stockcontroller.users.Stubs.userStub
import com.backendcoders.stockcontroller.users.controller.responses.LoginResponse
import com.backendcoders.stockcontroller.users.controller.responses.UserResponse
import io.kotest.matchers.collections.shouldContain
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import java.util.Optional

class UserServiceTest {

    private val repositoryMock = mockk<UserRepository>()
    private val roleRepositoryMock = mockk<RoleRepository>()
    private val jwt = mockk<Jwt>()

    private val service = UserService(repositoryMock, roleRepositoryMock,jwt)
    @BeforeEach
    fun setup(){
        clearAllMocks()
    }
    @AfterEach
    fun cleanUp(){
        checkUnnecessaryStub(repositoryMock,roleRepositoryMock,jwt)
    }

    @Test
    fun `insert must throw BadRequestException if user already exists`(){
        val user = userStub()
        every{
            repositoryMock.findByEmail(user.email)
        } returns user
        assertThrows<BadRequestException> {
            service.insert(user)
        } shouldHaveMessage ("User Already Exists!")
    }

    @Test
    fun `insert must save if user doenst exist`(){
        val user = userStub(id=null)
        every{
            repositoryMock.findByEmail(user.email)
        } returns null
        val userSaved = userStub()
        every { repositoryMock.save(user) } returns userSaved
        service.insert(user) shouldBe userSaved
    }

    @Test
    fun `findAll should request an ascending list if SortDir ASC is used`(){
        val sortDir = SortDir.ASC
        val userList = listOf(userStub(1), userStub(2), userStub(3))
        every { service.findAll(sortDir) } returns userList
        service.findAll(sortDir) shouldBe userList
    }

    @Test
    fun `findAll should request an descending list if SortDir DESC is used`(){
        val sortDir = SortDir.DESC
        val userList = listOf(userStub(1), userStub(2), userStub(3))
        every { service.findAll(sortDir) } returns userList
        service.findAll(sortDir) shouldBe userList
    }

    @Test
    fun `findByIdOrNull should delegate to repository`(){
        val user = userStub()
        every{
            repositoryMock.findById(any())
        } returns Optional.of(user)
        service.findByIdOrNull(1) shouldBe user
    }

    @Test
    fun `delete must throw NotFoundException if there's no user to delete`()
    {
        every {
            repositoryMock.findById(any())
        } returns Optional.empty()
        assertThrows<NotFoundException> {
            service.delete(1) shouldBe false
        }
    }

    @Test
    fun `delete must delete user and return true if user is deleted`(){
        val user = userStub()
        every { repositoryMock.findById(any()) } returns Optional.of(user)
        justRun { repositoryMock.delete(user) }
        service.delete(1) shouldBe true
    }

    @Test
    fun `delete cant delete the last user with ADMIN Role`(){
        every { repositoryMock.findByIdOrNull(1) } returns userStub(roles = listOf("ADMIN"))
        every { repositoryMock.findByRole("ADMIN") } returns listOf(userStub(roles = listOf("ADMIN")))

        assertThrows<BadRequestException> {
            service.delete(1)
        } shouldHaveMessage ("Cannot delete the last system administrator!")
    }

    @Test
    fun `update must return null if the name is the same`(){
        val user = userStub(nome = "Usuario")
        every { repositoryMock.findById(1) } returns Optional.of(user)

        service.update(1, user.name) shouldBe null
    }

    @Test
    fun `update update and save the user with slot and capture `(){
        val user = userStub()
        val slot = slot<User>()
        val saved = userStub(id = 1, nome = "name")
        every { repositoryMock.findById(any()) } returns Optional.of(user)
        every{ repositoryMock.save(capture(slot))} returns saved
        service.update(1,"name") shouldBe saved
        saved.name shouldBe "name"
        slot.isCaptured shouldBe true
        slot.captured.name shouldBe "name"
    }

    @Test
    fun `update throw NotFoundException if there's no user`(){

        every { repositoryMock.findById(any()) } returns Optional.empty()
        assertThrows<NotFoundException> {
            service.update(1,userStub().name)
        }
    }

    @Test
    fun `AddRole must return false if the user already have the role`(){
        val user = userStub(roles = listOf("ADMIN"))
        every{
            repositoryMock.findById(1)
        } returns Optional.of(user)

        service.addRole(1, "ADMIN") shouldBe false
    }

    @Test
    fun `AddRole must throw BadRequestException if the role doenst exist`(){

        every{
            roleRepositoryMock.findByName(any())
        } returns null
        every { repositoryMock.findById(1) } returns Optional.of(userStub())
        assertThrows<BadRequestException> {
            service.addRole(1,"AdminMock")
        } shouldHaveMessage "Invalid role: AdminMock"
    }

    @Test
    fun `AddRole must throw NotFoundException if not find user`(){
        every {
            repositoryMock.findById(1)
        } returns Optional.empty()
        assertThrows<NotFoundException> {
            service.addRole(1,"ADMIN")
        }
    }
    @Test
    fun `AddRole add a role to an user`(){
        val user= userStub()
        val role= roleStub()
        every{
            repositoryMock.findById(any())
        } returns Optional.of(user)
        every {
            roleRepositoryMock.findByName(any())
        } returns role
        every { repositoryMock.save(user) } returns user

        service.addRole(1,role.name) shouldBe true
        user.roles shouldContain role
    }
    @Test
    fun `login should return null if the email doenst exists`(){

        every{
            repositoryMock.findByEmail(any())
        } returns null
        service.login(userStub().email, userStub().password) shouldBe null
    }
    @Test
    fun `login should return null if the password doenst match`(){
        val user = userStub()
        every{
            repositoryMock.findByEmail(any())
        } returns user

        service.login(user.email, "wrongpassword") shouldBe null
    }

    @Test
    fun `login must return the credentials if the login is correct`(){
        val user = userStub()
        every { repositoryMock.findByEmail(user.email) } returns user
        every { jwt.createToken(user) } returns "token"

        service.login(user.email,user.password) shouldBe LoginResponse("token", UserResponse(user))
    }








}