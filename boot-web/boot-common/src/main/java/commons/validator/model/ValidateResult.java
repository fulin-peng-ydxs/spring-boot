package commons.validator.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 校验结果对象
 * @author pengshuaifeng
 * 2023/12/23
 */
@Data
public class ValidateResult {

    //校验状态
    private boolean status;
    //校验异常消息
    private String message;
    //校验异常集合
    private List<ValidateError> errors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateError{
        //字段名
        private String field;
        //字段中文名
        private String fieldCn;
        //校验异常消息
        private String message;
    }
}
