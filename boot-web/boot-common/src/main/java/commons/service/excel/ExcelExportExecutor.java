package commons.service.excel;

import commons.model.annotations.excel.ExcelExport;
import commons.utils.ClassUtils;
import commons.utils.ExcelUtils;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Excel导出执行器
 *
 * @author fulin-peng
 * 2024-08-30  15:32
 */
public abstract class ExcelExportExecutor {

    /**
     * 导出
     * 2024/8/30 下午4:03
     * @param method 方法对象
     * @param data 要导出的数据集（method返回值）
     * @param isXlsx 是否导出xlsx类型，否则为xls
     * @author fulin-peng
     * @return excel的字节数组
     */
    public  byte[] exportByte(Method method,Object data,boolean isXlsx) throws Exception {
        ExcelExport excelExport = method.getAnnotation(ExcelExport.class);
        if(excelExport !=null){
            //获取数据集
            Collection<?> rows;   //从返回值里获取真正的数据集对象
            String dataAttributeName = excelExport.dataAttributeName();
            if(data instanceof Collection){
                rows=(Collection<?>)data;
            }else{
                Object object = ClassUtils.getFieldValueWithMultistage(dataAttributeName, data);
                rows=(Collection<?>) object;
            }
            if(rows==null || rows.isEmpty())  //校验数据集
                return null;
            //定义表头的映射：header=key:表头名 => value:字段名
            Class<?> rowClass = rows.iterator().next().getClass();
            Map<String, String> header;
            String[] headerEnNames = excelExport.headerEnNames();
            if(headerEnNames.length!=0){
                String[] headerCnNames = excelExport.headerCnNames();
                header = new LinkedHashMap<>();
                for (int i = 0; i < headerEnNames.length; i++) {
                    header.put(headerCnNames[i],headerEnNames[i]);
                }
            }else{
                header= ExcelUtils.generateHeaders(rowClass, excelExport.fieldNames().length==0?null: Arrays.asList(excelExport.fieldNames()),
                        excelExport.fieldIgnoreNames().length==0?null:Arrays.asList(excelExport.fieldIgnoreNames()));
            }
            //字典转换
            convertDict(rows,rowClass,header);
            //写入excel
            return convertData(rows,header,isXlsx,excelExport);
        }
        return null;
    }


    /**
     * 生成exel
     * 2024/8/30 下午4:13
     * @param rows 数据集集
     * @param header key：表头-value：字段映射
     * @param isXlsx 是否导出xlsx类型，否则为xls
     * @param excelExport ExcelExport注解对象
     * @return excel的字节数组
     * @author fulin-peng
     */
    protected byte[] convertData(Collection<?> rows, Map<String, String> header,boolean isXlsx,ExcelExport excelExport) throws Exception {
        return ExcelUtils.writeToBytes(Collections.singletonMap(excelExport.sheetName(), rows),
                Collections.singletonMap(excelExport.sheetName(), header), excelExport.headerIndex(), excelExport.startRowIndex(),
                excelExport.startColIndex(), isXlsx ? ExcelUtils.ExcelType.XLSX : ExcelUtils.ExcelType.XLS, null);
    }


    /**
     * 字典转换
     * 2024/8/30 下午3:58
     * @param rows 数据集集
     * @param rowClass 数据集集元素对象类型
     * @param header key：表头-value：字段映射
     * @author fulin-peng
     */
    protected abstract void convertDict(Collection<?> rows,Class<?> rowClass,Map<String, String> header);
}
