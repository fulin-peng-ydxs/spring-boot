package commons.model.exception;


import lombok.Getter;

/**
 * 通用系统根异常
 *
 * @author pengshuaifeng
 * 2024/1/21
 */
@Getter
public class GeneralSystemRootException extends RuntimeException{

    public GeneralSystemRootException(String exceptionMsg,Exception e) {
        super(exceptionMsg,e);
    }
}