package com.caowei.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandle {

	@ExceptionHandler(UserLoginException.class)
	public ResponseEntity<String> handleUserLoginException(Exception ex) {
		String jsonString="{\"msg\":\""+ex.getMessage()+"\"}";
		return new ResponseEntity<String>(jsonString, HttpStatus.OK);
	}
}
