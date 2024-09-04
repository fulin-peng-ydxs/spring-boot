package commons.resolver.excel;

import commons.model.annotations.Dict;
import commons.model.annotations.excel.ExcelMark;
import commons.model.entity.dict.BasicDict;
import commons.utils.ClassUtils;
import commons.utils.CollectionUtils;
import commons.utils.ExcelUtils;
import commons.utils.StringUtils;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel解析器
 *
 * @author fulin-peng
 * 2024-09-03  09:51
 */
public abstract class ExcelMarkResolver {

    /**
     * 导出
     * 2024/8/30 下午4:03
     * @param method 方法对象
     * @param data 要导出的数据集（method返回值）
     * @param isXlsx 是否导出xlsx类型，否则为xls
     * @author fulin-peng
     * @return excel的字节数组
     */
    public  byte[] exportByte(Method method, Object data, boolean isXlsx)  {
        ExcelMark excelMark = method.getAnnotation(ExcelMark.class);
        if(excelMark !=null && excelMark.resolverType()==ResolverType.EXPORT){
            //获取数据集
            Collection<?> rows;   //从返回值里获取真正的数据集对象
            String dataAttributeName = excelMark.dataAttributeName();
            if(data instanceof Collection){
                rows=(Collection<?>)data;
            }else{
                Object object = ClassUtils.getFieldValueWithMultistage(dataAttributeName, data);
                rows=(Collection<?>) object;
            }
            if(CollectionUtils.isEmpty(rows))  //校验数据集
                return null;
            //定义表头的映射：header=key:表头名 => value:字段名
            Class<?> rowClass = rows.iterator().next().getClass();
            Map<String, String> header=resolveHeader(excelMark,rowClass,ResolverType.EXPORT);
            //字典转换
            convertDict(rows,rowClass,null,header,ResolverType.EXPORT);
            //写入excel
            return convertData(rows,header,isXlsx, excelMark);
        }else if(excelMark !=null){
            throw new RuntimeException("无法处理:["+"ExcelMark.resolverType()="+excelMark.resolverType()+"]");
        }
        return null;
    }


    /**
     * 生成exel
     * 2024/8/30 下午4:13
     * @param rows 数据集集
     * @param header key：表头-value：字段映射
     * @param isXlsx 是否导出xlsx类型，否则为xls
     * @param excelMark ExcelExport注解对象
     * @return excel的字节数组
     * @author fulin-peng
     */
    protected abstract byte[] convertData(Collection<?> rows, Map<String, String> header, boolean isXlsx, ExcelMark excelMark);


    /**
     * 导入
     * 2024/9/3 上午11:03 
     * @author fulin-peng
     */
    public  Collection<?> importData(InputStream in,Parameter parameter){
        ExcelMark excelMark = parameter.getAnnotation(ExcelMark.class);
        if(excelMark !=null  && excelMark.resolverType()==ResolverType.IMPORT){
            //定义表头的映射：header=key:表头名 => value:字段名
            Class<?> rowClass = parameter.getType();
            if (ClassUtils.typeEquals(rowClass, Collection.class)) {  //集合类型，获取泛型
                rowClass=ClassUtils.getActualTypeArguments(parameter.getParameterizedType()).get(0);
            }
            Map<String,String> header=resolveHeader(excelMark,rowClass,ResolverType.IMPORT);
            header=header==null?new HashMap<>():header;
            //获取excel数据
            Map<String, Object> convertData = convertData(in, rowClass, header, excelMark);
            //字典转换
            Collection<Object> data = ExcelUtils.getResultCollection(convertData, excelMark.sheetName());
            convertDict(data,rowClass,null,header,ResolverType.IMPORT);
            return data;
        }else if(excelMark !=null){
            throw new RuntimeException("无法处理:["+"ExcelMark.resolverType()="+excelMark.resolverType()+"]");
        }
        return null;
    }

    /**
     * 获取exel数据
     * 2024/8/30 下午4:13
     * @param in excel流
     * @param clazz 数据集集元素对象类型
     * @param header key：表头-value：字段映射
     * @param excelMark ExcelExport注解对象
     * @return 结果集
     * @author fulin-peng
     */
    protected abstract Map<String,Object> convertData(InputStream in, Class<?> clazz, Map<String, String> header, ExcelMark excelMark);


    /**
     * 字典转换
     * 2024/8/30 下午3:58
     * @param rows 数据集集
     * @param rowClass 数据集集元素对象类型
     * @param headerFields 表头字段集
     * @param header key：表头-value：字段映射
     * @param type 解析类型：ResolverType
     * @author fulin-peng
     */
    protected void convertDict(Collection<?> rows, Class<?> rowClass,List<Field> headerFields,Map<String, String> header,ResolverType type){
        //获取要导出的字典字段和字典值的映射
        Map<Dict, List<Field>> exportFields= null;
        List<Field> fields;
        if(CollectionUtils.isNotEmpty(headerFields)){
            fields=headerFields.stream().filter(field->field.getAnnotation(Dict.class) != null)
                    .collect(Collectors.toList());
        }else{
           fields =header.values().stream().filter(value -> {  //筛出含有Dict注解的字段
                Field field = ClassUtils.getField(rowClass,value);
                return field.getAnnotation(Dict.class) != null;
            }).map(value -> {
                return ClassUtils.getField(rowClass,value);  //将字段名转换为字段类对象
            }).collect(Collectors.toList());
        }
        if(!fields.isEmpty()){
            //字段导出目标转换：调整导出头映射&生成导出字段和字典值的映射
            exportFields=new HashMap<>();
            Map<String, String> headerEn=new HashMap<>();
            if (type== ResolverType.EXPORT)
                header.forEach((key,value)-> headerEn.put(value,key));
            for (Field dictField : fields) {
                Dict dict = dictField.getAnnotation(Dict.class);
                String nameEn = dict.nameEn();
                String fieldName = dictField.getName();
                if (StringUtils.isNotEmpty(nameEn) && !nameEn.equals(fieldName)){
                    if (type== ResolverType.EXPORT)
                        header.put(headerEn.get(fieldName), nameEn);
                    exportFields.put(dict, Arrays.asList(dictField,
                            ClassUtils.getField(rowClass,nameEn)));
                }else exportFields.put(dict, Collections.singletonList(dictField));
            }
        }
        //根据字典字段和字典值的映射进行字典值的转换
        if(exportFields!=null){
            //加载字典数据
            Map<String, Map<String, BasicDict>> dictDataMap = exportFields.keySet().stream()
                    .map(Dict::value)
                    .collect(Collectors.toMap(key -> key, value -> {
                        Map<String, BasicDict> dictMap = type==ResolverType.EXPORT?loadExportDict(value):loadImportDict(value);
                        if (dictMap == null) {
                            return Collections.emptyMap();
                        }
                        return dictMap;
                    }));
            //遍历数据，转换字典数据
            for (Object row : rows) {
                exportFields.forEach((key,value)->{
                    Field sourceField =  value.get(0);
                    Field targetField = value.size() > 1 ? value.get(1) : value.get(0);
                    Map<String, BasicDict> dictMap =dictDataMap.get((key.value()));
                    if(!dictMap.isEmpty()){
                        if(type==ResolverType.EXPORT){
                            convertExportDict(row,sourceField,targetField,dictMap);
                        }else{
                            convertImportDict(row,sourceField,targetField,dictMap);
                        }
                    }
                });
            }
        }
    }


    /**
     * 转换导出字典
     * 2024/9/3 上午10:06
     * @param row 数据行
     * @param sourceField 源字典字段
     * @param targetField 字典导出目标字段
     * @param dictMap 导出字典数据
     * @author fulin-peng
     */
    protected void convertExportDict(Object row,Field sourceField, Field targetField,Map<String,BasicDict> dictMap){
        Object fieldValue = ClassUtils.getFieldValue(sourceField, row);
        String dictCode = fieldValue==null?"":fieldValue instanceof String?fieldValue.toString():String.valueOf(fieldValue);
        BasicDict basicDict = dictMap.get(dictCode);
        String targetValue= basicDict==null?dictCode:basicDict.getDictValue();
        ClassUtils.setFieldValue(targetField,targetValue,row);
    }

    /**
     * 转换导入字典
     * @param row 数据行
     * @param targetField 字典导入目标字段
     * @param sourceField 源字典字段
     * @param dictMap 导入字典数据
     * 2024/9/3 上午10:50 
     * @author fulin-peng
     */
    protected void convertImportDict(Object row,Field targetField, Field sourceField,Map<String,BasicDict> dictMap){
        Object fieldValue = ClassUtils.getFieldValue(sourceField, row);
        String dictValue = fieldValue==null?"":fieldValue instanceof String?fieldValue.toString():String.valueOf(fieldValue);
        BasicDict basicDict = dictMap.get(dictValue);
        if(basicDict!=null){
            Class<?> targetFieldType = targetField.getType();
            Object targetValue=basicDict.getDictKey();
            if(targetFieldType!=String.class){
                targetValue=ClassUtils.invokeMethod(null,
                        org.springframework.util.ClassUtils.getMethod(targetFieldType,"valueOf",String.class),targetValue);
            }
            ClassUtils.setFieldValue(targetField,targetValue,row);
        }
    }

    /**
     * 加载导出字典
     * 2024/9/3 上午10:51 
     * @author fulin-peng
     */
    protected Map<String, BasicDict> loadExportDict(String dictName){
        return null;
    }

    /**
     * 加载导入字典
     * 2024/9/3 上午10:55 
     * @author fulin-peng
     */
    protected Map<String, BasicDict> loadImportDict(String dictName){
        return null;
    }


    /**
     * 解析表头
     * 2024/9/3 下午1:59
     * @param excelMark excel标志对象
     * @param clazz 数据集集元素对象类型
     * @param resolverType  解析类型：ResolverType
     * @author fulin-peng
     */
    protected Map<String,String> resolveHeader(ExcelMark excelMark,Class<?> clazz,ResolverType resolverType){
        Map<String, String> header;
        String[] headerEnNames = excelMark.headerEnNames();
        if(headerEnNames.length!=0){
            String[] headerCnNames = excelMark.headerCnNames();
            header = new LinkedHashMap<>();
            for (int i = 0; i < headerEnNames.length; i++) {
                header.put(headerCnNames[i],headerEnNames[i]);
            }
        }else{
            header= resolverType==ResolverType.EXPORT?
            ExcelUtils.generateHeaders(clazz, excelMark.fieldNames().length==0?null: Arrays.asList(excelMark.fieldNames()),
                    excelMark.fieldIgnoreNames().length==0?null:Arrays.asList(excelMark.fieldIgnoreNames()))
                    : null;
        }
        return header;
    }
    

    /**
     * 解析类型
     */
    public enum ResolverType{
        EXPORT,

        IMPORT
    }
}
