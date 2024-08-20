package commons.model.web.response;

import lombok.Getter;

/**
 * 响应状态类型
 * 2023/12/20 21:32
 * @author pengshuaifeng
 */
@Getter
public enum ResponseStatus {

    SUCCESS("SUCCESS","请求成功"),
    ERROR("ERROR","系统异常"),
    BUSINESS_FAILURE("BUSINESS_FAILURE","业务错误"),
    AUTHENTICATION_FAILURE("AUTHENTICATION_FAILURE","认证失败"),
    AUTHORIZATION_FAILURE("AUTHORIZATION_FAILURE","鉴权失败"),
    REQUEST_PATH_FAILURE("REQUEST_PATH_FAILURE","请求路径错误"),
    PARAMS_CHECK_FAILURE("PARAM_CHECK_FAILURE","参数校验不通过");

    private final String status;
    private final String message;

    ResponseStatus(String status,String message){
        this.status=status;
        this.message=message;
    }
}
