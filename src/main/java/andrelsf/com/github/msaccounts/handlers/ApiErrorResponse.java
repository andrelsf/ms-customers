package andrelsf.com.github.msaccounts.handlers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse {

  private final Integer code;
  private String message;

  public ApiErrorResponse(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ApiErrorResponse contactSysAdmin() {
    return new ApiErrorResponse(INTERNAL_SERVER_ERROR.value(), "Contact SysAdmin");
  }

  public String getMessage() {
    return message;
  }
}
