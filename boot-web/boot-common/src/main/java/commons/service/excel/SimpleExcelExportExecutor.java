package commons.service.excel;

import commons.model.annotations.Dict;
import commons.model.entity.dict.BasicDict;
import commons.utils.ClassUtils;
import commons.utils.StringUtils;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 简单ExcelExportExecutor实现
 * @author fulin-peng
 * 2024-08-30  15:44
 */
@Component
public class SimpleExcelExportExecutor extends ExcelExportExecutor {

    //缓存字典字段：需要导出的字典字段和字典值的映射
    private final Map<Class<?>, Map<Dict,List<Field>>> cacheDictFields=new HashMap<>();

    @Override
    protected void convertDict(Collection<?> rows, Class<?> rowClass, Map<String, String> header) {
        //获取要导出的字典字段和字典值的映射
        Map<Dict,List<Field>> exportFields=cacheDictFields.get(rowClass);
        if(exportFields==null){
            List<Field> fields = header.values().stream().filter(value -> {  //筛出含有Dict注解的字段
                Field field = ClassUtils.getField(rowClass,value);
                return field.getAnnotation(Dict.class) != null;
            }).map(value -> {
                return ClassUtils.getField(rowClass,value);  //将字段名转换为字段类对象
            }).collect(Collectors.toList());
            if(!fields.isEmpty()){
                //字段导出目标转换：调整导出头映射&生成导出字段和字典值的映射
                exportFields=new HashMap<>();
                Map<String, String> headerEn = new HashMap<>();
                header.forEach((key,value)-> headerEn.put(value,key));
                for (Field dictField : fields) {
                    Dict dict = dictField.getAnnotation(Dict.class);
                    String nameEn = dict.nameEn();
                    String fieldName = dictField.getName();
                    if (StringUtils.isNotEmpty(nameEn) && !nameEn.equals(fieldName)){
                        header.put(headerEn.get(fieldName), nameEn);
                        exportFields.put(dict,Arrays.asList(dictField,
                                ClassUtils.getField(rowClass,nameEn)) );
                    }else exportFields.put(dict, Collections.singletonList(dictField));
                }
                cacheDictFields.put(rowClass, exportFields);
            }
        }
        //根据字典字段和字典值的映射进行字典值的转换
        if(exportFields!=null){
            //加载字典数据
            Map<String, Map<String, BasicDict>> dictDataMap = exportFields.keySet().stream()
                    .map(Dict::value)
                    .collect(Collectors.toMap(key -> key, value -> {
                        Map<String, BasicDict> dictMap = loadDictMap(value);
                        if (dictMap == null) {
                            return Collections.emptyMap();
                        }
                        return dictMap;
                    }));
            //遍历导出数据，转换字典数据
            for (Object row : rows) {
                exportFields.forEach((key,value)->{
                    Field sourceField =  value.get(0);
                    Field targetField = value.size() > 1 ? value.get(1) : value.get(0);
                    Map<String, BasicDict> dictMap =dictDataMap.get((key.value()));
                    String targetValue="";
                    if(!dictMap.isEmpty()){
                        Object fieldValue = ClassUtils.getFieldValue(sourceField, row);
                        String dictCode = fieldValue==null?"":fieldValue instanceof String?fieldValue.toString():String.valueOf(fieldValue);
                        BasicDict basicDict = dictMap.get(dictCode);
                        targetValue= basicDict==null?dictCode:basicDict.getDictValue();
                    }
                    ClassUtils.setFieldValue(targetField,targetValue,row);
                });
            }
        }
    }

    //TODO 获取字典数据由实际系统实现
    protected Map<String,BasicDict> loadDictMap(String dictName){ return null;}

}
