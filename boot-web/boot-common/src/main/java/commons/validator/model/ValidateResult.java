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
@NoArgsConstructor
@AllArgsConstructor
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


    public static ValidateResult success(){
        return new ValidateResult(true,null, null);
    }

    public static ValidateResult failure(String message){
        return new ValidateResult(false,message, null);
    }

    public static ValidateResult failure(String message,List<ValidateError> errors){
        return new ValidateResult(false,message,errors);
    }

    public static ValidateResult failure(List<ValidateError> errors,String errorPrefix){
        errorPrefix = errorPrefix == null ? "" : errorPrefix;
        StringBuilder messageBuilder=new StringBuilder();
        for (ValidateError error : errors) {
            messageBuilder.append(error.message);
            messageBuilder.append(errorPrefix);
        }
        String message = messageBuilder.toString();
        message=message.lastIndexOf(errorPrefix)==-1?message:message.substring(0,message.length()-1);
        return new ValidateResult(false,message,errors);
    }

    public static ValidateResult failure(List<ValidateError> errors){
        return failure(errors, "|");
    }
}
