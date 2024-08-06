package andrelsf.com.github.msaccounts.handlers;

import andrelsf.com.github.msaccounts.handlers.exceptions.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiRestHandlerExceptions extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ApiRestHandlerExceptions.class);

  @ExceptionHandler(CustomerNotFoundException.class)
  protected ResponseEntity<ApiErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<ApiErrorResponse> handlerRuntimeException(RuntimeException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiErrorResponse.contactSysAdmin());
  }
}
