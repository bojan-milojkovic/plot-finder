package com.plot.finder.util;

import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.exception.UsernameNotFoundException;

@ControllerAdvice
public class MyControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badRequest_JsonFields(MethodArgumentNotValidException ex){
		
		MyBadInputResponse bir = new MyBadInputResponse("Some fields in your request body are invalid", 
                ex.getMessage().split("((?<=[)], with [0-9]{1,2} error[(]s[)]):)|((?<=[)]) (?=throws))")[0]);
		
		 bir.setValidationErrors(
	                ex.getBindingResult().getFieldErrors()
	                                     .stream()
	                                     .map(l -> l.getDefaultMessage())
	                                     .collect(Collectors.toList()));
		 
		return bir;
	}
	
	@ExceptionHandler(MyRestPreconditionsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badRequest_TakenValues(MyRestPreconditionsException ex) {
		MyBadInputResponse bir = new MyBadInputResponse(ex.getDescription(), ex.getDetails());
		
		bir.setValidationErrors(ex.getErrors());

		return bir;
	}
	
	@ExceptionHandler({JsonMappingException.class, EmptyResultDataAccessException.class, EntityNotFoundException.class, HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public MyBadInputResponse badRequest_MissingEntity(RuntimeException ex) {
		return new MyBadInputResponse("You are attempting to process an invalid object.", 
				ex.getLocalizedMessage());
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public MyBadInputResponse badCredentials(UsernameNotFoundException ex){
		return new MyBadInputResponse("Your credentials are invalid.", 
				ex.getLocalizedMessage());
	}
	
	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badNumberInput(NumberFormatException ex) {
		return new MyBadInputResponse("Invalid numerical value", 
				ex.getLocalizedMessage());
	}
}