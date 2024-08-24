package web.mvc.proxy.before;


import commons.model.annotations.validate.EntityValid;
import commons.model.exception.ExceptionType;
import commons.model.exception.GeneralBusinessException;
import commons.validator.ValidatorService;
import io.swagger.annotations.ApiModelProperty;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

/**
 * 后置处理器
 *
 * @author pengshuaifeng
 * 2024/1/20
 */
@Component
public class ProxyBeforeExecutor {

    @Autowired
    private ValidatorService validatorService;


    /**
     * 前置处理
     * @author fulin peng
     * 2023/8/3 0003 17:41
     */
    public Object before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //参数校验
        validate(method,joinPoint.getArgs());
        return null;
    }

    /**
     * 参数校验
     * 2024/1/10 0010 18:17
     * @author fulin-peng
     */
    //TODO 目前仅支持异常抛出方式响应，后面是否能够基于注解本身去动态选择处理方式
    public void validate(Method method, Object[] args){
        Parameter[] parameters = method.getParameters();
        //遍历每个方法参数，进行校验处理
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            //处理具有EntityValid注解标识的参数
            EntityValid annotation = AnnotationUtils.findAnnotation(parameter, EntityValid.class);
            if(annotation!=null){
                Object targetArg=args[i];
                //校验对象是否为空
                if(targetArg==null){
                    if (!annotation.required()) {
                        continue;
                    }
                    String message;
                    NotNull notNull = AnnotationUtils.findAnnotation(parameter, NotNull.class);
                    if(notNull!=null){
                        message=notNull.message();
                    }else{
                        String paramName;
                        ApiModelProperty apiModelProperty = AnnotationUtils.findAnnotation(parameter, ApiModelProperty.class);
                        if(apiModelProperty!=null){
                            paramName=apiModelProperty.value();
                        }else{
                            paramName=parameter.getName();
                        }
                        message="参数\""+paramName+"\"不能为空";
                    }
                    throw new GeneralBusinessException(ExceptionType.PARAMS_CHECK_FAILURE,message);
                }else{ //校验对象不为空，则调用校验器服务进行校验
                    if (targetArg instanceof Collection) {
                        validatorService.validates(((Collection<?>)targetArg),true);
                    }else{
                        validatorService.validate(targetArg,true);
                    }
                }
            }
        }
    }



}
