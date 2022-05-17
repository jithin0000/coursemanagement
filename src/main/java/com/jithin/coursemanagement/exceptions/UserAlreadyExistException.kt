package com.jithin.coursemanagement.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserAlreadyExistException(message: String):RuntimeException(message) {

}
