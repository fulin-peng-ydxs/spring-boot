package commons.model.annotations.log;


import commons.model.enums.GeneralOperateStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogSave {

    /**日志类型*/
    String logType();

    /**日志模板：
     * -{xxx-{name1}-{res.name2}-xxx}
     * -xxx-%s-%s-xxxx
     * -{xxx-{name1}-%s-xxx}
     * */
    String logDescriptionTemplate();

    /**日志object参数对象参数索引：使用{}时生效*/
    int logObjectParamIndex() default 0;

    /**日志参数索引：使用%s时生效 ，0,1,2,3 ==> xx(p1,p2,p3,p4)*/
    String logParamIndex() default "0";

    /**日志object返回状态属性名称*/
    String logObjectReturnStatusField() default "";
    /**日志object返回状态属性值*/
    String logObjectReturnStatusValue() default GeneralOperateStatus.SUCCESS;
}
