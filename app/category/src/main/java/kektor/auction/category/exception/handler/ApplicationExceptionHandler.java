package kektor.auction.category.exception.handler;

import jakarta.validation.ConstraintViolationException;
import kektor.auction.category.exception.CategoryNotFoundException;
import kektor.auction.category.exception.RestrictParentDeletionException;
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

    @ExceptionHandler(CategoryNotFoundException.class)
    ErrorResponse handleResourceNotFoundException(CategoryNotFoundException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .property("resource", "Category")
                .property("categoryId", ex.getCategoryId())
                .build();
    }

    @ExceptionHandler(RestrictParentDeletionException.class)
    ErrorResponse handleRestrictParentDeletionException(RestrictParentDeletionException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .property("parentId", ex.getParentId())
                .build();
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

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
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


    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse handleInvalidDatabaseRequestException(
            Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleInvalidCategoryException(
            ConstraintViolationException ex) {
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ErrorResponse handleAll(Exception ex) {
        return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, Optional.ofNullable(ex.getMessage())
                .orElse("Internal error"));
    }


    //    @ExceptionHandler({ Exception.class })
//    public ResponseEntity<Object> handleAll(
//            Exception ex,
//            WebRequest request) {
//
//
//        return ResponseEntityBuilder.build(err);
//    }


//    private ResponseEntity<ErrorResponse> buildErrorResponse(
//            Exception exception,
//            HttpStatus httpStatus,
//            WebRequest request
//    ) {
//        return buildErrorResponse(
//                exception,
//                exception.getMessage(),
//                httpStatus,
//                request);
//    }
//
//    private ResponseEntity<ErrorResponse> buildErrorResponse(
//            Exception exception,
//            String message,
//            HttpStatus httpStatus,
//            WebRequest request
//    ) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                httpStatus.value(),
//                exception.getMessage()
//        );
//
//        if(printStackTrace && isTraceOn(request)){
//            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
//        }
//        return ResponseEntity.status(httpStatus).body(errorResponse);
//    }
//
//    private boolean isTraceOn(WebRequest request) {
//        String [] value = request.getParameterValues(TRACE);
//        return Objects.nonNull(value)
//                && value.length > 0
//                && value[0].contentEquals("true");
//    }
//}

}
