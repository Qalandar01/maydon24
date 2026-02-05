package uz.ems.maydon24.exeption;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.response.ErrorCode;
import uz.ems.maydon24.response.ErrorResponse;
import uz.ems.maydon24.response.ValidationErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .errorCode(ErrorCode.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .errorCode(ErrorCode.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleExists(AlreadyExistsException ex) {
        return ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .errorCode(ErrorCode.ALREADY_EXISTS)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ValidationErrorResponse.builder()
                .success(false)
                .message("Validation error")
                .fieldErrors(errors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(Exception ex) {
        return ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .errorCode(ErrorCode.INTERNAL_ERROR)
                .build();
    }
}
