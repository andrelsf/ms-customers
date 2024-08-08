package andrelsf.com.github.msaccounts.handlers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import andrelsf.com.github.msaccounts.handlers.exceptions.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiRestHandlerExceptions {

  private static final Logger log = LoggerFactory.getLogger(ApiRestHandlerExceptions.class);

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(CONFLICT)
        .body(new ApiErrorResponse(CONFLICT.value(), ex.getMessage()));
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(NOT_FOUND)
        .body(new ApiErrorResponse(NOT_FOUND.value(), ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiErrorResponse> handlerMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(BAD_REQUEST)
        .body(new ApiErrorResponse(BAD_REQUEST.value(), "Invalid parameter"));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiErrorResponse> handlerRuntimeException(RuntimeException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(ApiErrorResponse.contactSysAdmin());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiErrorResponse> handlerException(Exception ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(ApiErrorResponse.contactSysAdmin());
  }
}
