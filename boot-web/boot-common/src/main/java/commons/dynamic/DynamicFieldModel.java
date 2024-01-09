package commons.dynamic;

import commons.utils.ClassUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 动态字段模型：用于描述不同实体的所有字段信息
 * @author fulin-peng
 * 2023-11-30  14:30
 */
@Data
public class DynamicFieldModel {


    /**
     * 构造方法：字段信息解析
     * 2024/1/2 0002 15:52
     * @author fulin-peng
     */
    public DynamicFieldModel(Object value, Field field, boolean hidden){
        this.hidden=hidden;
        this.fieldName = field.getName();
        this.fieldValue= ClassUtils.getFieldValue(fieldName,value,Object.class);
        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
        if(apiModelProperty!=null){
            fieldNameCn=apiModelProperty.value();
        }
    }
    /**
     * 生成实体某个字段信息模型
     * @param value 实体对象
     * @param field 实体字段类对象
     * @param hidden 是否需要隐藏该字段
     * 2024/1/2 0002 15:54
     * @author fulin-peng
     */
    public static DynamicFieldModel generate(Object value, Field field,boolean hidden){
        return new DynamicFieldModel(value,field,hidden);
    }

    /**
     * 生成实体字段信息模型集
     * @param value 实体对象
     * @param ignoreFields 需要忽略的字段集
     * @param containFields 仅解析此字段集
     * 2024/1/2 0002 15:54
     * @author fulin-peng
     */
    public static List<DynamicFieldModel> generate(Object value,List<String> ignoreFields,List<String> containFields){
        List<DynamicFieldModel> dynamicFieldModels = new LinkedList<>();
        if(value.getClass()==Object.class)
            return dynamicFieldModels;
        for (Field declaredField : value.getClass().getDeclaredFields()) {
            String fieldName = declaredField.getName();
            if(containFields!=null && !containFields.contains(fieldName)){
                continue;
            }else if(ignoreFields!=null && ignoreFields.contains(fieldName)){
                continue;
            }
            Class<?> fieldType = declaredField.getType();
            if(Collection.class.isAssignableFrom(fieldType)){//集合递归生成
                DynamicFieldModel generate = generate(value, declaredField, false);
                Object collectionValues = generate.getFieldValue();
                if (collectionValues!=null){
                    List<List<DynamicFieldModel>> values = new LinkedList<>();
                    for (Object collectionValue : ((Collection<?>) collectionValues)) {
                        values.add(generate(collectionValue,ignoreFields,containFields));
                    }
                    generate.setFields(values);
                }
                dynamicFieldModels.add(generate);
            }else{  //普通生成
                dynamicFieldModels.add(generate(value,declaredField,false));
            }
        }
        return dynamicFieldModels;
    }

    public static List<DynamicFieldModel> generate(Object value){
        return generate(value,null,null);
    }

    //字段名
    private String fieldName;
    //字段值
    private Object fieldValue;
    //字段中文名
    private String fieldNameCn;
    //是否需要展示
    private boolean hidden;
    //字段集合
    private List<List<DynamicFieldModel>> fields;
    //后续可根据需要持续扩展
}
