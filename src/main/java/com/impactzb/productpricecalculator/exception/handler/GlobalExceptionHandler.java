package com.impactzb.productpricecalculator.exception.handler;

import com.impactzb.productpricecalculator.data.dto.ProblemDetails;
import com.impactzb.productpricecalculator.exception.ApiBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String BAD_REQUEST_TITLE = "Bad Request";

    @ExceptionHandler({ApiBadRequestException.class})
    public ResponseEntity<Object> handleApiBadRequestException(final Exception ex) {
        log.error("Exception thrown", ex);
        ApiBadRequestException appException = (ApiBadRequestException) ex;
        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title(BAD_REQUEST_TITLE)
                .details(List.of(appException.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(problemDetails, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .title(BAD_REQUEST_TITLE)
                .details(errorList)
                .build();
        return handleExceptionInternal(ex, problemDetails, headers, problemDetails.getStatus(), request);
    }
}
