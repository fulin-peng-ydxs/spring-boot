package commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * class工具类
 * author: pengshuaifeng
 * 2023/12/18
 */
public class ClassUtils {

    /**获取类的泛型
     * 2022/9/7 0007-15:17
     * @author pengfulin
     * @param source 类对象
     * @return 返回类型集合
     */
    public static List<Class<?>> getParamTypes(Class<?> source){
        List<Class<?>> paramTypes=null;
        Type genericSuperclass = source.getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            paramTypes=new LinkedList<>();
            for (Type typeArgument : ((ParameterizedType) genericSuperclass).getActualTypeArguments()) {
                paramTypes.add((Class<?>)typeArgument);
            }
        }
        return paramTypes;
    }

    /**
     * 获取字段：多级字段
     * 2023/11/9 0009 12:05
     * @param fieldName 字段名 xx.xx.~
     * @param value 对象
     * @return 字段对象
     */
    public static <T> T  getFieldValueWithMultistage(String fieldName,Object value){
        try {
            if (fieldName.contains(".")) {
                String[] fields = fieldName.split("\\.");
                for (String field : fields) {
                    Object fieldValue = getFieldValue(field, value);
                    if (fieldValue==null)
                        return null;
                    value=fieldValue;
                }
                return (T)value;
            }else{
                return getFieldValue(fieldName,value);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取字段异常",e);
        }
    }


    /**
     * 获取字段值
     * 2023/11/9 0009 12:05
     * @author fulin-peng
     */
    public static  <T> T getFieldValue(String fieldName,Object value,Class<T> filedType){
        try {
            Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T)field.get(value);
        } catch (Exception e) {
            throw new RuntimeException("获取字段异常",e);
        }
    }

    public static  <T> T getFieldValue(Field field, Object value, Class<T> filedType){
        return getFieldValue(field.getName(), value, filedType);
    }


    public static  <T> T getFieldValue(String fieldName,Object value){
        try {
            Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T)field.get(value);
        } catch (Exception e) {
            throw new RuntimeException("获取字段异常",e);
        }
    }

    public static  <T> T getFieldValue(Field field, Object value){
        return getFieldValue(field.getName(), value,null);
    }


    /**
     * 设置字段值
     * 2023/12/7 0007 16:20
     * @author fulin-peng
     */
    public static void setFieldValue(String fieldName,Object fieldValue,Object value){
        try {
            Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(value,fieldValue);
        } catch (Exception e) {
            throw new RuntimeException("设置字段异常",e);
        }
    }

    /**
     * 设置字段值
     * 2023/12/7 0007 16:20
     * @author fulin-peng
     */
    public static void setFieldValue(Field field ,Object fieldValue,Object value){
        try {
            field.setAccessible(true);
            field.set(value,fieldValue);
        } catch (Exception e) {
            throw new RuntimeException("设置字段异常",e);
        }
    }


    /**
     * 获取枚举对象
     * @param enumType 枚举的class对象
     * @param enumValue 枚举值的字符串
     * 2023/12/23 17:10
     * @author pengshuaifeng
     */
    public static  <T extends Enum<T>> T getEnum(Class<T> enumType, String enumValue){
        return Enum.valueOf(enumType,enumValue);
    }

    /**
     * 查找属性
     * @param clazz 类对象
     * @param  needSupper 是否需要包含父对象
     * @param  fields 属性集合
     * 2023/12/24 22:54
     * @author pengshuaifeng
     */
    public static List<Field> getSupperClassFields(Class<?> clazz,boolean needSupper,List<Field> fields){
        if(fields==null)
            fields=new LinkedList<>();
        if(clazz!=Object.class){
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            Class<?> superclass = clazz.getSuperclass();
            if (superclass!=Object.class && needSupper) {
                return getSupperClassFields(superclass,true,fields);
            }
        }
        return fields;
    }

    /**
     * 获取字段
     * 2023/12/24 22:54
     * @param clazz 类对象
     * @param  fieldName 字段名称
     * @author pengshuaifeng
     */
    public static Field getField(Class<?> clazz,String fieldName){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException("获取字段异常",e);
        }
    }

    /**
     * 类型是否一致
     * @param clazz1 同类或子类
     * @param clazz2 同类或父类
     * 2023/12/24 23:20
     * @author pengshuaifeng
     */
    public static boolean typeEquals(Class<?> clazz1,Class<?> clazz2){
        return clazz1 == clazz2 || clazz2.isAssignableFrom(clazz1);
    }


}
