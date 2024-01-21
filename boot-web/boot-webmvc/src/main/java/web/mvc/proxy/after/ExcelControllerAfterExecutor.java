package web.mvc.proxy.after;


import commons.holder.ServletHolder;
import commons.model.annotations.excel.ExcelDownload;
import commons.model.web.mime.MimeType;
import commons.model.web.response.Response;
import commons.utils.ExcelUtils;
import commons.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * excel控制器后置处理器
 *
 * @author pengshuaifeng
 * 2024/1/21
 */
@Component
public class ExcelControllerAfterExecutor extends ControllerAfterExecutor{


    @Override
    protected Object execute(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String accept = ServletHolder.getRequestHeader("x-download-excel");
            if(accept==null || result==null)
                return result;
            if (accept.equals(MimeType.APPLICATION_XLSX) || accept.equals(MimeType.APPLICATION_XLS)) {  //请求接受excel响应
                ExcelDownload excelDownload = method.getAnnotation(ExcelDownload.class);
                if(excelDownload!=null){   //定义了excel导出注解
                    Collection<?> rows;   //从返回值里获取真正的数据集对象
                    try {
                        String dataAttributeName = excelDownload.dataAttributeName();
                        if(result instanceof Response){
                            Object body = ((Response) result).getBody();
                            if(StringUtils.isNotEmpty(dataAttributeName)){
                                Field declaredField = body.getClass().getDeclaredField(dataAttributeName);
                                declaredField.setAccessible(true);
                                body= declaredField.get(body);
                            }
                            rows=(Collection<?>) body;
                        }else if(result instanceof Collection){
                            rows=(Collection<?>)result;
                        }else{
                            //TODO 后续可递归查找
                            Field declaredField = result.getClass().getDeclaredField(dataAttributeName);
                            declaredField.setAccessible(true);
                            rows=(Collection<?>) declaredField.get(result);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("未获取excel导出的数据集",e);
                    }
                    if(rows==null || rows.isEmpty())  //响应数据不为空
                        return null;
                    Class<?> rowClass = rows.iterator().next().getClass();   //定义导出sheet模型
                    Map<String, String> header;
                    String[] headerEnNames = excelDownload.headerEnNames();  //定义表头的映射：包含哪些字段&字段的映射关系
                    if(headerEnNames.length!=0){
                        String[] headerCnNames = excelDownload.headerCnNames();
                        header = new LinkedHashMap<>();
                        for (int i = 0; i < headerEnNames.length; i++) {
                            header.put(headerCnNames[i],headerEnNames[i]);
                        }
                    }else{
                        header=ExcelUtils.generateHeaders(rowClass, excelDownload.fieldNames().length==0?null: Arrays.asList(excelDownload.fieldNames()),
                                excelDownload.fieldIgnoreNames().length==0?null:Arrays.asList(excelDownload.fieldIgnoreNames()));
                    }
                    //字典值转换
                    convertDict(rows,header,excelDownload.ignoreDictField(),rowClass);
                    //导出数据
                    boolean isXlsx = !accept.equals(MimeType.APPLICATION_XLS);
                    byte[] bytes = ExcelUtils.writeToBytes(Collections.singletonMap(excelDownload.sheetName(), rows),
                            Collections.singletonMap(excelDownload.sheetName(),header),excelDownload.headerIndex(),excelDownload.startRowIndex(),
                            excelDownload.startColIndex(),isXlsx ? ExcelUtils.ExcelType.XLSX : ExcelUtils.ExcelType.XLS, null);
                    ServletHolder.responseToOutStream(null,bytes,excelDownload.fileName(),isXlsx?MimeType.APPLICATION_XLSX:MimeType.APPLICATION_XLS);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("excel数据导出异常",e);
        }
    }

    /**
     * 转换字典值
     * 2024/1/21 20:44
     * @author pengshuaifeng
     */
    //TODO 需要实际系统应用才能实现
    protected void convertDict(Collection<?> rows,Map<String, String> header,boolean ignoreDictField,Class<?> rowClass){
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
