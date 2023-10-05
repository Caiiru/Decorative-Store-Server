package com.backendcoders.stockcontroller.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus
import java.awt.event.FocusEvent.Cause

@ResponseStatus(NOT_FOUND)
class NotFoundException(
    message:String="Not Found",
    cause: Throwable? =null
): IllegalArgumentException(message,cause){
    constructor(id:Long):this("Not Found. Id=&id")
}
