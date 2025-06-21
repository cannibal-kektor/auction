package kektor.auction.orchestrator.exception.handler;

import jakarta.validation.ConstraintViolationException;
import kektor.auction.orchestrator.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

    @ExceptionHandler(TooEarlyBidException.class)
    public ErrorResponse handleTooEarlyBidException(TooEarlyBidException ex) {
        return ErrorResponse.builder(ex, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())
                .property("lotId", ex.getLotId())
                .property("bidAttemptTime", ex.getBidAttemptTime())
                .property("auctionStartTime", ex.getAuctionStart())
                .build();
    }

    @ExceptionHandler(TooLateBidException.class)
    public ErrorResponse handleTooLateBidException(TooLateBidException ex) {
        return ErrorResponse.builder(ex, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())
                .property("lotId", ex.getLotId())
                .property("bidAttemptTime", ex.getBidAttemptTime())
                .property("auctionEndTime", ex.getAuctionEnd())
                .build();
    }

    @ExceptionHandler(TooLowBidAmountException.class)
    public ErrorResponse handleTooLowBidAmountException(TooLowBidAmountException ex) {
        return ErrorResponse.builder(ex, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())
                .property("lotId", ex.getLotId())
                .property("attemptAmount", ex.getAttemptAmount())
                .property("lowerLimitAmountExclusive", ex.getLowerLimitBidAmountExclusive())
                .build();
    }

    @ExceptionHandler(NotEnoughAccountFundException.class)
    public ErrorResponse handleNotEnoughAccountFundException(NotEnoughAccountFundException ex) {
        return ErrorResponse.builder(ex, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())
                .property("userId", ex.getUserId())
                .property("amount", ex.getRequestedAmount())
                .build();
    }


    @ExceptionHandler(StaleLotVersionException.class)
    public ErrorResponse handleStaleLotVersionException(
            StaleLotVersionException ex) {
        return ErrorResponse.builder(ex, HttpStatus.CONFLICT, ex.getMessage())
                .property("lotId", ex.getLotId())
                .property("currentLotVersion", ex.getCurrentVersion())
                .property("submittedVersion", ex.getSubmittedVersion())
                .property("sagaId", ex.getSagaId())
                .build();
    }

    @ExceptionHandler(ConcurrentSagaException.class)
    public ErrorResponse handleConcurrentSagaException(
            ConcurrentSagaException ex) {
        return ErrorResponse.builder(ex, HttpStatus.CONFLICT, ex.getMessage())
                .property("lotId", ex.getLotId())
                .property("triedCreatedOn", ex.getTriedCreatedOn())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse handleDataAccessException(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
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


    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setDetail("Request timeout");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .headers(headers)
                .body(problemDetail);
    }


    @ExceptionHandler(Exception.class)
    ErrorResponse handleAll(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, Optional.ofNullable(ex.getMessage())
                .orElse("Internal error"));
    }
}
