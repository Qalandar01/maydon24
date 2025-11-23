package uz.ems.maydon24.exeption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.ems.maydon24.models.dto.request.Empty;
import uz.ems.maydon24.models.dto.response.ErrorResponse;
import uz.ems.maydon24.models.dto.response.Response;

import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map((e) -> e.getField() + " " + e.getDefaultMessage()).toList();
        StringBuilder sb = new StringBuilder();
        errors.forEach(s -> sb.append(s).append(System.getProperty("line.separator")));
        String errorMessage = !sb.toString().isEmpty() ? sb.toString() : ex.getMessage();

        var error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        var responseData = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
        // Handle the InvalidFormatException here
        String errorMessage = "Invalid format for field " + ex.getPath().get(0).getFieldName();
        var error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        var responseData = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleGeneralExceptions(Exception ex) {
        var error = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
        var responseData = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<?> handleRuntimeExceptions(Exception ex) {
        var error = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();

        var responseData = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        var error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        var responseData = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public final ResponseEntity<?> handleJsonProcessingException(JsonProcessingException ex) {
        var error = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error while converting object to string: " + ex.getMessage())
                .build();

        var responseData = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        log.error("ErrorStatus: {}, Message: {} ", ex.getCode(), ex.getMessage());
        var error = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        var response = Response.builder()
                .error(error)
                .data(Empty.builder().build())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}