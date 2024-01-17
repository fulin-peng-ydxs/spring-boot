package commons.model.annotations.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体校验注解标识
 * 2023/12/24 15:08
 * @author pengshuaifeng
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityValid {

    boolean required() default true;

}
