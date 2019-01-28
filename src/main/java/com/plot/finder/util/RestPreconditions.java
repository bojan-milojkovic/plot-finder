package com.plot.finder.util;

import com.plot.finder.exception.MyRestPreconditionsException;

public class RestPreconditions {
	
	public static void checkStringIsValid(final String s, final String message) throws MyRestPreconditionsException {
		assertTrue(checkString(s), message, "Given string value is invalid");
	}

	public static void checkSuchEntityAlreadyExists(Object entity, String message) throws MyRestPreconditionsException{
		if(entity!=null) {
			throw new MyRestPreconditionsException("The value you have entered is already being used by another user.", message);
		}
	}
	
	public static void assertTrue(boolean condition, String message) throws MyRestPreconditionsException {
		if(!condition) {
			throw new MyRestPreconditionsException("Assertion failed for this operation.", message);
		}
	}
	
	public static void assertTrue(boolean condition, String title, String message) throws MyRestPreconditionsException{
		if(!condition) {
			throw new MyRestPreconditionsException(title, message);
		}
	}
	
	public static boolean checkString(final String s){
		return s!=null && !s.trim().isEmpty();
	}
	
	public static boolean checkStringMatches(final String s, final String regExp){
		return s!=null && s.matches(regExp);
	}
	
	public static void verifyStringFormat(final String s, final String format, final String message, final String details) 
			throws MyRestPreconditionsException {
		if(!checkStringMatches(s, format)) {
			throw new MyRestPreconditionsException(message, details);
		}
	}
	
	
	public static <T> T checkNotNull(T object, String description) throws MyRestPreconditionsException {
		if(object!=null){
			return object;
		}
		throw new MyRestPreconditionsException("Cannot find the object specified", description);
	}
	
	public static <T> T checkNotNull(T object, String description, String details)throws MyRestPreconditionsException {
		if(object!=null){
			return object;
		}
		throw new MyRestPreconditionsException(description, details);
	}
	
	public static void checkId(final Long id) throws MyRestPreconditionsException {
		if(!(id!=null && id>0)){
			throw new MyRestPreconditionsException("Find object by id failed", "Id is invalid");
		}
	}
}
