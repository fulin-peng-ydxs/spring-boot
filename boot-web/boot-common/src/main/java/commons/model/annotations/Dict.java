package commons.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**字典value*/
    String value();
    /**字典字段中文名：用作excel处理时，如果不为空，会优先使用作真正的导入或导出字段映射*/
    String nameCn() default "";
    /**字典字段英文名：用作excel处理时，如果不为空，会优先使用作真正的导入或导出字段*/
    String nameEn() default "";
}