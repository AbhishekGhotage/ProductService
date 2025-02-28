package com.example.productservice.controlleradvice;


import com.example.productservice.dtos.ExceptionDto;
import com.example.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<String> handleArithmeticException(ArithmeticException e) {
//        return ResponseEntity.badRequest().body(e.getMessage());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                "Something went wrong, Coming from controller advice",
                HttpStatus.BAD_REQUEST
        );
        return responseEntity;
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<String> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e){
//        return ResponseEntity.badRequest().body(e.getMessage());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                "Array index out of bounds, Coming from controller advice",
                HttpStatus.BAD_REQUEST
        );
        return responseEntity;
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e){
//        return ResponseEntity.badRequest().body(e.getMessage());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                "Null pointer exception, Coming from controller advice",
                HttpStatus.BAD_REQUEST
        );
        return responseEntity;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDto> handlerProductNotFoundException(ProductNotFoundException e){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());
        exceptionDto.setSolution("Try again with different product id");

        ResponseEntity<ExceptionDto> response = new ResponseEntity<>(
                exceptionDto,
                HttpStatus.NOT_FOUND
        );

        return response;
    }
}