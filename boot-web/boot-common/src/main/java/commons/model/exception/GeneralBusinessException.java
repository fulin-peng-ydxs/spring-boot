package commons.model.exception;


import lombok.Getter;

/**
 * 通用业务异常
 *
 * @author pengshuaifeng
 * 2024/1/21
 */
@Getter
public class GeneralBusinessException extends RuntimeException{

    private final ExceptionType exceptionType;

    private final Object exceptionBody;

    public GeneralBusinessException(String exceptionMsg,ExceptionType exceptionType,Object exceptionBody) {
        super(exceptionMsg);
        this.exceptionType=exceptionType;
        this.exceptionBody=exceptionBody;
    }

    public GeneralBusinessException(ExceptionType exceptionType,Object exceptionBody) {
       this(exceptionType.getMessage(),exceptionType,exceptionBody);
    }

    public GeneralBusinessException(String exceptionMsg,ExceptionType exceptionType,Exception t,Object exceptionBody) {
        super(exceptionMsg,t);
        this.exceptionType=exceptionType;
        this.exceptionBody=exceptionBody;
    }

    public GeneralBusinessException(ExceptionType exceptionType,Exception t,Object exceptionBody) {
        this(exceptionType.getMessage(),exceptionType,t,exceptionBody);
    }
}
