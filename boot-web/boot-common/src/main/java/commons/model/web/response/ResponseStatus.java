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

    ERROR("ERROR","系统异常，请重试"),

    PARAMS_CHECK_FAILURE("PARAM_CHECK_FAILURE","参数校验不通过"),

    BUSINESS_FAILURE("BUSINESS_FAILURE","系统业务异常，请联系管理员");

    private final String status;

    private final String message;

    ResponseStatus(String status,String message){
        this.status=status;
        this.message=message;
    }
}
