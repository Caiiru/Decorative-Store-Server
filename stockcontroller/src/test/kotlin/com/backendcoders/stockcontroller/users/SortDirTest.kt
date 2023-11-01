package com.backendcoders.stockcontroller.users

import com.backendcoders.stockcontroller.exception.BadRequestException
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SortDirTest {

    @Test
    fun `findOrThrow must throw BAD REQUEST if a invalid sort dir is passed`(){
        val error = assertThrows<BadRequestException> {
            SortDir.findOrThrow("invalid")
        }
        error shouldHaveMessage "Invalid Sort Dir"
    }

    @ParameterizedTest
    @ValueSource(strings = ["ASC","asc","Asc"])
    fun `findOrThrow must return ASC ignoring case`(value:String){
        SortDir.findOrThrow(value) shouldBe SortDir.ASC
    }

    @ParameterizedTest
    @ValueSource(strings = ["DESC","desc","Desc"])
    fun `findOrThrow must return DESC ignoring case`(value: String){
        SortDir.findOrThrow(value) shouldBe SortDir.DESC
    }
}