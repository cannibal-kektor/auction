package kektor.auction.payment.exception.handler;

import jakarta.validation.ConstraintViolationException;
import kektor.auction.payment.exception.PaymentAccountNotFoundByIdException;
import kektor.auction.payment.exception.PaymentAccountNotFoundByUserIdException;
import kektor.auction.payment.exception.StalePaymentAccountVersionException;
import kektor.auction.payment.model.PaymentAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PaymentAccountNotFoundByIdException.class)
    ErrorResponse handleResourceNotFoundException(PaymentAccountNotFoundByIdException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .property("resource", PaymentAccount.class.getSimpleName())
                .property("accountId", ex.getAccountId())
                .build();
    }


    @ExceptionHandler(PaymentAccountNotFoundByUserIdException.class)
    ErrorResponse handleBidNotFoundBySagaException(PaymentAccountNotFoundByUserIdException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .property("resource", PaymentAccount.class.getSimpleName())
                .property("userId", ex.getUserId())
                .build();
    }

    @ExceptionHandler(StalePaymentAccountVersionException.class)
    ErrorResponse handleStaleLotVersionException(StalePaymentAccountVersionException ex) {
        return ErrorResponse.builder(ex, HttpStatus.CONFLICT, ex.getMessage())
                .property("accountId", ex.getPaymentAccountId())
                .property("currentAccountVersion", ex.getCurrentVersion())
                .property("submittedVersion", ex.getSubmittedVersion())
                .property("sagaId", ex.getSagaId())
                .build();
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    ErrorResponse handleOptimisticLockException(OptimisticLockingFailureException ex) {
        return ErrorResponse.create(ex, HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ErrorResponse handleInvalidBidException(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    ErrorResponse handleInvalidDatabaseRequestException(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers,
                                                                        HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setDetail("Request timeout");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .headers(headers)
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String detail = ex
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError)
                        return String.join("", fieldError.getField(), " : ",
                                error.getDefaultMessage());
                    else {
                        return error.getDefaultMessage();
                    }
                })
                .collect(Collectors.joining(";"));
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        return ResponseEntity
                .badRequest()
                .headers(headers)
                .body(problemDetail);
    }


    @ExceptionHandler(Exception.class)
    ErrorResponse handleAll(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, Optional.ofNullable(ex.getMessage())
                .orElse("Internal error"));
    }


}
