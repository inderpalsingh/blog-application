package com.blogapi.exceptions;


public class ResourceNotFoundException extends RuntimeException{

    String resourceName, fieldName;
    long fieldValue;


    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
