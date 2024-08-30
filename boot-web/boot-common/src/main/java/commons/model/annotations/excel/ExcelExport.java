package commons.model.annotations.excel;

import commons.service.excel.ExcelExportExecutor;
import commons.service.excel.SimpleExcelExportExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {
    /**
     * 导出文件名
     */
    String fileName() default "导出表";
    /**
     * 指定导出的字段，为空默认导出全部（表头名默认取swagger中ApiModelProperty注解属性，没有则取字段名称）
     */
    String[] fieldNames() default {};
    /**
     * 指定导出忽略的字段，为空默认导出全部,fieldNames和fieldIgnoreNames都不为空则，则一起生效
     */
    String[] fieldIgnoreNames() default {};
    /**
     * 指定导出的表头字段映射-字段名（不为空，则fieldNames和fieldIgnoreNames属性失效）
     */
    String[] headerEnNames() default {};
    /**
     * 指定导出的表头字段映射-中文名（与headerEnNames一一对应）
     */
    String[] headerCnNames() default {};

    /**
     * 指定sheet名
     */
    String sheetName() default "数据列表";
    /**
     * 数据集字段名：支持多级：xx.xxx.xxxx
     */
    String dataAttributeName() default "";
    /**
     * 数据写入起始行索引
     */
    int startRowIndex() default 1;
    /**
     * 数据写入起始列索引
     */
    int startColIndex() default 0;
    /**
     * 表头行索引
     */
    int headerIndex() default 0;
    /**
     * excel导出处理实现类
     */
    Class<? extends ExcelExportExecutor> executorClass() default SimpleExcelExportExecutor.class;
}
