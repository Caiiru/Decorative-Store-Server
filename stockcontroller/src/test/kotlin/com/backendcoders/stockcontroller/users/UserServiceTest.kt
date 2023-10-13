package com.backendcoders.stockcontroller.users
import com.backendcoders.stockcontroller.exception.BadRequestException
import com.backendcoders.stockcontroller.exception.NotFoundException
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.backendcoders.stockcontroller.users.Stubs.userStub
import org.springframework.data.domain.Sort
import java.util.Optional

class UserServiceTest {

    private val repositoryMock = mockk<UserRepository>()
    private val service = UserService(repositoryMock)

    @BeforeEach
    fun setup(){
        clearAllMocks()
    }
    @AfterEach
    fun cleanUp(){
        checkUnnecessaryStub(repositoryMock)
    }

    @Test
    fun `insert must throw BadRequestException if an user with the same email is found`(){
        val user = Stubs.userStub(id = null)
        every{
            repositoryMock.findByEmail(user.email)
        }returns Stubs.userStub()
        assertThrows<BadRequestException> {
            service.insert(user)
        } shouldHaveMessage ("User Already Exists!")

    }

    @Test
    fun `insert must return the saved user if its inserted`(){
        val user = Stubs.userStub()
        every{
            repositoryMock.findByEmail(user.email)
        } returns null
        val saved = Stubs.userStub()
        every { repositoryMock.save(user) } returns saved
        service.insert(user) shouldBe saved
    }

    @Test
    fun `findAll should request an ascending list if sort dir ASC is used to the repository`()
    {
        val userList = listOf(userStub(1, nome = "Abigail"), userStub(2, nome = "Maicon"))
        every { repositoryMock.findAll(Sort.by("name").ascending()) }returns userList

        service.findAll(SortDir.ASC) shouldBe userList
    }
    @Test
    fun `findAll should request an descending list if sort dir DESC is used to the repository`()
    {
        val userList = listOf(userStub(1, nome = "Abigail"), userStub(2, nome = "Maicon"))
        every { repositoryMock.findAll(Sort.by("name").descending()) }returns userList

        service.findAll(SortDir.DESC) shouldBe userList
    }

    @Test
    fun `findByIdOrNull should delegate to the repository`(){

        val user = userStub()
        every { repositoryMock.findById(1) } returns Optional.of(user)
        service.findByIdOrNull(1) shouldBe user
    }
    @Test
    fun `deleteById must return false if doenst exist user`(){

        every{ repositoryMock.findById(1)} returns Optional.empty()
        assertThrows<NotFoundException> {
            service.delete(1)
        } shouldHaveMessage "Not Found. Id=&id"

    }

    @Test
    fun `deleteById Must Return true and delete the user if exists`(){
        val user= userStub()
        every{
            repositoryMock.findById(1)
        } returns Optional.of(user)
        justRun { repositoryMock.delete(user) }
        service.delete(1) shouldBe true
    }

    @Test
    fun `update throw not found exception if the user doenst exist`(){
        every { repositoryMock.findById(1) }returns Optional.empty()
        assertThrows<NotFoundException> { service.update(1, userStub()) }

    }
    @Test
    fun `update must returns null if there's no change`(){
        val user = userStub()
        every { repositoryMock.findById(1) } returns Optional.of(user)
        service.update(1, user) shouldBe null
    }

    @Test
    fun `update update and save the user`(){
        val user = userStub()
        every { repositoryMock.findById(1) } returns Optional.of(user)
        val saved = userStub(id = 1, nome = "name")
        val slot = slot<User>()
        every { repositoryMock.save(capture(slot)) } returns saved
        service.update(1, userStub(nome = "name")) shouldBe saved
        slot.isCaptured shouldBe true
        slot.captured.name shouldBe saved.name
    }






}