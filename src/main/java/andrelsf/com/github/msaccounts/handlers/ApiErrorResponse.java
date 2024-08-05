package andrelsf.com.github.msaccounts.handlers;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse {

  private Integer code;
  private String message;

  public ApiErrorResponse(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ApiErrorResponse contactSysAdmin() {
    return new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(), "Contact SysAdmin");
  }

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
