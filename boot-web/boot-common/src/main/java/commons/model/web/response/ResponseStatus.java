package commons.model.web.response;

import lombok.Getter;

/**
 * 响应状态类型
 * 2023/12/20 21:32
 * @author pengshuaifeng
 */
@Getter
public enum ResponseStatus {

    SUCCESS("success","请求成功"),
    ERROR("error","系统异常"),
    BUSINESS_FAILURE("business_failure","业务错误"),
    AUTHENTICATION_FAILURE("authentication_failure","认证失败"),
    AUTHORIZATION_FAILURE("authorization_failure","鉴权失败"),
    REQUEST_PATH_FAILURE("request_path_failure","请求路径错误"),
    PARAMS_CHECK_FAILURE("param_check_failure","参数校验不通过");

    private final String status;
    private final String message;

    ResponseStatus(String status,String message){
        this.status=status;
        this.message=message;
    }
}
