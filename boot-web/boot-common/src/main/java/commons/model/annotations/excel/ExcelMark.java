package commons.model.annotations.excel;


import commons.resolver.excel.ExcelMarkResolver;
import commons.resolver.excel.SimpleExcelMarkExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 导入&导出处理标记
 * 2024/9/3 上午11:29 
 * @author fulin-peng
 */

@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelMark {

    /**
     * 导出或导入文件名
     */
    String fileName() default "table";
    /**
     * 指定导出或导入的字段，为空默认导出全部（表头名默认取swagger中ApiModelProperty注解属性，没有则取字段名称）
     */
    String[] fieldNames() default {};
    /**
     * 指定导出或导入忽略的字段，为空默认导出全部,fieldNames和fieldIgnoreNames都不为空，则优先使用fieldNames
     */
    String[] fieldIgnoreNames() default {};
    /**
     * 指定导出或导入的表头字段映射-字段名（不为空，则fieldNames和fieldIgnoreNames属性失效）
     */
    String[] headerEnNames() default {};
    /**
     * 指定导出或导入的表头字段映射-中文名（与headerEnNames一一对应）
     */
    String[] headerCnNames() default {};

    /**
     * 指定sheet名
     */
    String sheetName() default "data";
    /**
     * 数据集字段名：支持多级：xx.xxx.xxxx
     */
    String dataAttributeName() default "";
    /**
     * 数据写入或读取起始行索引
     */
    int startRowIndex() default 1;
    /**
     * 数据写入或读取起始列索引
     */
    int startColIndex() default 0;
    /**
     * 表头行索引
     */
    int headerIndex() default 0;

    /**
     * excel导出处理实现类
     */
    Class<? extends ExcelMarkResolver> exportClass() default SimpleExcelMarkExecutor.class;
    /**
     * excel 导入处理实现类
     */
    Class<? extends ExcelMarkResolver> importClass() default SimpleExcelMarkExecutor.class;

    /**
     * excel 处理类型：默认用作导出
     */
    ExcelMarkResolver.ResolverType resolverType() default ExcelMarkResolver.ResolverType.EXPORT;
}
