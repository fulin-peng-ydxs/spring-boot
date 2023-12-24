package web.mvc.proxy;

import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import validator.ValidatorService;
import validator.model.ValidateResult;
import web.mvc.model.annotations.proxy.EntityValid;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 校验代理
 * @author fulin peng
 * 2023/8/3 0003 16:45
 */
@Slf4j
@Aspect
@Service
public class ValidateProxy {

    @Autowired
    private ValidatorService validatorService;


    //TODO 目前仅支持异常抛出方式响应
    /**
     * Controller层的参数校验（类有@controller注解）
     * @author fulin peng
     * 2023/8/3 0003 17:41
     */
    @Before("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController))")
    public void beforeValid(JoinPoint joinPoint) {
        //获取方法参数
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        //遍历每个方法参数，进行校验处理
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            //处理具有EntityValid注解标识的参数
            EntityValid annotation = AnnotationUtils.findAnnotation(parameter, EntityValid.class);
            if(annotation!=null){
                Object targetArg=joinPoint.getArgs()[i];
                //校验对象是否为空
                if(targetArg==null){
                    if (!annotation.required()) {
                        continue;
                    }
                    String message=null;
                    NotNull notNull = AnnotationUtils.findAnnotation(parameter, NotNull.class);
                    if(notNull!=null){
                        message=notNull.message();
                    }else{
                        String paramName=null;
                        ApiModelProperty apiModelProperty = AnnotationUtils.findAnnotation(parameter, ApiModelProperty.class);
                        if(apiModelProperty!=null){
                            paramName=apiModelProperty.value();
                        }else{
                            paramName=parameter.getName();
                        }
                        message="参数\""+paramName+"\"不能为空";
                    }
                    throw new ValidationException(message);
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
