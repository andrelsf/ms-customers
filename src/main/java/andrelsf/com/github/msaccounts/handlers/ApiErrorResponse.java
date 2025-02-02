package andrelsf.com.github.msaccounts.handlers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ApiErrorResponse {

  private Integer code;
  private String message;

  public ApiErrorResponse(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ApiErrorResponse contactSysAdmin() {
    return new ApiErrorResponse(INTERNAL_SERVER_ERROR.value(), "Contact SysAdmin");
  }

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
