package commons.validator;

import commons.model.exception.ExceptionType;
import commons.model.exception.GeneralBusinessException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import commons.validator.model.ValidateResult;
import javax.validation.Validator;
import java.util.*;
/**
 * 校验服务
 * @author pengshuaifeng
 * 2023/12/22
 */
@Getter
@Slf4j
@Service
public class ValidatorService {

    @Autowired
    private Validator defaultValidator;

    private  final String defaultErrorPrefix="|";

    /**
     * 校验
     * @param object 待校验的对象：支持一般实体和实体集合单个校验
     * 2023/12/24 13:00
     * @author pengshuaifeng
     */
    public ValidateResult validate(Object object) {
        return validate(object,false);
    }

    /**
     * 校验
     * @param object 待校验的对象：支持一般实体和实体集合单个校验
     * @param isThrow 是否抛出异常
     * 2023/12/23 19:21
     * @author pengshuaifeng
     */
    public ValidateResult validate(Object object,boolean isThrow){
        ValidateResult validateResult;
        if (object instanceof Collection) {
            validateResult = null;
            for (Object o : ((Collection<?>) object)) {
                validateResult = validate(defaultValidator, o, defaultErrorPrefix, false);
                if (validateResult.isStatus()){
                    continue;
                }
                if(isThrow){
                    throw new GeneralBusinessException(ExceptionType.DATA_CHECK_FAILURE,validateResult.getMessage());
                }else{
                    return validateResult;
                }
            }
        }else{
            validateResult = validate(defaultValidator, object, defaultErrorPrefix, false);
            if (!validateResult.isStatus() && isThrow) {
                throw new GeneralBusinessException(ExceptionType.DATA_CHECK_FAILURE,validateResult.getMessage());
            }
        }
        return validateResult;
    }

    /**
     * 校验
     * @param objects 待校验的对象：实体集合
     * 2023/12/23 19:21
     * @author pengshuaifeng
     */
    public List<ValidateResult> validates(Collection<?> objects){
        return validates(objects,false);
    }

    /**
     * 校验
     * @param objects 待校验的对象：实体集合
     * @param isThrow 是否抛出异常，抛出异常会打断校验
     * 2023/12/23 19:21
     * @author pengshuaifeng
     */
    public List<ValidateResult> validates(Collection<?> objects, boolean isThrow){
        LinkedList<ValidateResult> validateResults = new LinkedList<>();
        for (Object object : objects) {
            ValidateResult validateResult = validate(defaultValidator, object, defaultErrorPrefix, false);
            if (!validateResult.isStatus()) {
                if(isThrow){
                    throw new GeneralBusinessException(ExceptionType.DATA_CHECK_FAILURE,validateResult.getMessage());
                }else{
                    validateResults.add(validateResult);
                }
            }
        }
        return validateResults;
    }


    /**
     * 校验
     * @param obj 待校验对象：实体
     * @param errorPrefix 异常消息拼接前缀
     * @param singleton 是否仅校验单个：如果是，则出现一个异常后即结束
     * 2023/12/23 22:15
     * @author pengshuaifeng
     */
    public ValidateResult validate(Object obj,String errorPrefix,boolean singleton){
        return validate(defaultValidator,obj,errorPrefix,singleton);
    }

    /**
     * 校验
     * @param validator 校验器对象
     * 2023/12/23 21:21
     * @author pengshuaifeng
     */
    public static ValidateResult validate(Validator validator,Object obj,String errorPrefix,boolean singleton){
        //执行校验
        BindingResult bindingResult = new BeanPropertyBindingResult(obj, obj.getClass().getSimpleName());
        //TODO 目前仅支持spring的校验器对象，不支持自定义校验器对象
        ValidationUtils.invokeValidator((org.springframework.validation.Validator)validator,obj,bindingResult);
        //解析结果
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            LinkedList<ValidateResult.ValidateError> validateErrors = new LinkedList<>();
            for (FieldError error : errors) {
                //设置异常消息
                String defaultMessage = error.getDefaultMessage();
                //设置异常对象
                ValidateResult.ValidateError validateError = new ValidateResult.ValidateError();
                String field = error.getField();
                try {
                    ApiModelProperty apiModelProperty = obj.getClass().getDeclaredField(field).getAnnotation(ApiModelProperty.class);
                    String fieldName = apiModelProperty.value();
                    validateError.setFieldCn(fieldName);
                } catch (Exception e) {
                    log.debug("解析字段：{}中文名称异常",field,e);
                }
                validateError.setFieldCn(null);
                validateError.setField(field);
                validateError.setMessage(defaultMessage);
                validateErrors.add(validateError);
                if(singleton){
                    break;
                }
            }
            return ValidateResult.failure(validateErrors,errorPrefix);
        }return ValidateResult.success();
    }
}
