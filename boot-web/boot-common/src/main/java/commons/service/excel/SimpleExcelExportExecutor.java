package commons.service.excel;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
/**
 * 简单ExcelExportExecutor实现
 * @author fulin-peng
 * 2024-08-30  15:44
 */
@Component
public class SimpleExcelExportExecutor extends ExcelExportExecutor {

    @Override
    protected void convertDict(Collection<?> rows, Class<?> rowClass, Map<String, String> header) {
        //TODO 可加上自动忽略字典注解的导出，如果需要的话
        //                //字典数据加载：处理导出的字段中包含的字典值
        //                try {
        //                    Map<Field,Dict> dictFields=new HashMap<>(); //字典字段&字典配置集
        //                    Map<String, Map<String, SysDictionary>> dictMap = new HashMap<>(); // 字典的映射值
        //                    Set<Map.Entry<String, String>> headerSet = header.entrySet();
        //                    for (Map.Entry<String, String> entry : headerSet) {  //查询导出字段为字典字段集
        //                        Field field = rowClass.getDeclaredField(entry.getValue());  //原要导出的字段集
        //                        Dict dict = field.getAnnotation(Dict.class);
        //                        if(dict!=null) {   //字段为字典字段
        //                            dictFields.put(field, dict);
        //                            String dictExportField = dict.dictExportField();
        //                            if(StringUtils.isNotBlank(dictExportField))   //表头对应字段是否要转换为关联的导出字段，例如原字段为int类型，字典为字符类型时
        //                                header.put(entry.getKey(),dictExportField);  //替换原有的字段为关联的导出字段
        //                            Map<String, SysDictionary> map = sysDictionaryUtils.getSysDicMap(dict.dictName());  //查询并储存这个字典key的字典映射集
        //                            dictMap.put(dict.dictName(),map);
        //                        }
        //                    }
        //                    if (!dictFields.isEmpty()) {  //为导出的字典字段进行赋值
        //                        for (Object row : rows) {
        //                            for (Map.Entry<Field, Dict> fieldDictEntry : dictFields.entrySet()) {
        //                                Field field = fieldDictEntry.getKey();
        //                                field.setAccessible(true);
        //                                Dict dict = fieldDictEntry.getValue();
        //                                Object dictKey = field.get(row);
        //                                String dictValue = dictMap.get(dict.dictName()).get(dictKey.toString()).getDictValue();
        //                                if(StringUtils.isNotBlank(dict.dictExportField())){
        //                                    field=rowClass.getDeclaredField(dict.dictExportField());  //如果包含关联字段，则将字典映射值赋给关联字段
        //                                }
        //                                field.setAccessible(true);
        //                                field.set(row,dictValue);
        //                            }
        //                        }
        //                    }
        //                } catch (Exception e) {
        //                    throw new RuntimeException("excel导出的数据集字典数据转换异常",e);
        //                }
    }
}
