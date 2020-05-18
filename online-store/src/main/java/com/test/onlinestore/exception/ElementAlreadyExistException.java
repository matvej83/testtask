package com.test.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Such element already exists")
public class ElementAlreadyExistException extends RuntimeException{
}
