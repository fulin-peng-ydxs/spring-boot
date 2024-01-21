package commons.model.annotations.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDownload {
    //文件名
    String fileName() default "导出表";
    //指定导出的字段，为空默认导出全部（表头名默认取swagger中ApiModelProperty注解属性，没有则取字段名称）
    String[] fieldNames() default {};
    //指定导出忽略的字段，为空默认导出全部
    String[] fieldIgnoreNames() default {};
    //指定导出的表头字段映射-字段名（不为空，则fieldNames和fieldIgnoreNames属性失效）
    String[] headerEnNames() default {};
    //指定导出的表头字段映射-中文名
    String[] headerCnNames() default {};
    //忽略字典值的导出
    boolean ignoreDictField() default true;
    //指定sheet名
    String sheetName() default "数据列表";
    //数据集属性名
    String dataAttributeName() default "";
    //数据写入起始行
    int startRowIndex() default 1;
    //数据写入起始列
    int startColIndex() default 0;
    //表头行
    int headerIndex() default 0;
    //支持后续扩展
}
