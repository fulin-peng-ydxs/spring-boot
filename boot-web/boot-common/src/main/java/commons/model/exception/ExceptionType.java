package commons.model.exception;

import lombok.Getter;

/**
 * 异常类型
 * 2023/12/20 21:32
 * @author pengshuaifeng
 */
@Getter
public enum ExceptionType {

    PARAMS_CHECK_FAILURE("PARAM_CHECK_FAILURE","参数校验不通过"),

    DATA_CHECK_FAILURE("DATA_CHECK_FAILURE","数据校验不通过");

    private final String value;

    private final String message;

    ExceptionType(String value,String message){
        this.value=value;
        this.message=message;
    }

}
