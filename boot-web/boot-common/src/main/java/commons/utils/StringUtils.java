package commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * String字符串工具类
 *
 * @author peng_fu_lin
 * 2022-09-08 16:23
 */
public class StringUtils {


    /**是否为空
     * 2022/9/20 0020-13:48
     * @author pengfulin
    */
    public static boolean isEmpty(String value,boolean isNotTrim){
        if (value==null)
            return true;
        return isNotTrim ? value.isEmpty() : value.trim().isEmpty();
    }

    public static boolean isEmpty(String value){
       return isEmpty(value,true);
    }

    public static boolean isNotEmpty(String value){
        return !isEmpty(value);
    }


    /**清除指定字符
     * 2022/9/9 0009-15:47
     * @author pengfulin
     * @param value 待清除字符的字符串
     * @param charValue 待清除的字符
     * @param clearCharType 清除字符方式
     * @param clearCount 待清除字符的个数
     * <p>如果小于1，则会将字符串中所有的指定字符进行清除</p>
     * @return 返回清除指定字符后的字符串
    */
    public static String clearChar(String value, char charValue, ClearCharType clearCharType, int clearCount){
        StringBuilder builder = new StringBuilder();
        clearCount=clearCount<1? -1:clearCount;
        int count=0;
        if(clearCharType == ClearCharType.ALL){
            for (int i = 0; i < value.length(); i++) {
                if(value.charAt(i)==charValue&&(count<clearCount||clearCount==-1)){
                    count++;
                    continue;
                }
                builder.append(value.charAt(i));
            }
            return builder.toString();
        }
        int index=0;
        if(clearCharType == ClearCharType.START|| clearCharType == ClearCharType.NO_MIDDLE) {
            for (int i = 0; i < value.length(); i++) {
                index=i;
                if(value.charAt(i)==charValue&&(count<clearCount||clearCount==-1)){
                    count++;
                }else if(value.charAt(i)==charValue)
                    builder.append(value.charAt(i));
                else break;
            }
        }
        int length = value.length();
        if(clearCharType == ClearCharType.END|| clearCharType == ClearCharType.NO_MIDDLE) {
            for (int i = value.length()-1; i > 0; i--) {
                if(value.charAt(i)==charValue&&(count<clearCount||clearCount==-1)){
                    count++;
                    length--;
                } else
                    break;
            }
        }
        for (int i = index; i < length; i++) {
            builder.append(value.charAt(i));
        }
        return builderToString(builder);
    }

    /**
     * 清除首尾字符
     * 2023/9/20 23:20
     * @author pengshuaifeng
     */
    public static String clearChar(String value,char charValue,boolean fromHead){
        if (fromHead && value.charAt(0)==charValue) {
            return clearChar(value, File.separatorChar, ClearCharType.START, 1);
        }else if(!fromHead && value.charAt(value.length()-1)==charValue){
            return clearChar(value, File.separatorChar, ClearCharType.END, 1);
        }
        return value;
    }

    /**清除多个字符
     * 2022/9/19 0019-16:25
     * @author pengfulin
    */
    public static String clearChars(String value, ClearCharType clearCharType, char ...charValue){
        for (char c : charValue) {
            value=clearChar(value, c, clearCharType, -1);
        }
        return value;
    }

    /**
     * 清除首尾空白符：空格、制表符、换行符
     * 2023/9/9 00:50
     * @author pengshuaifeng
     */
    public static String cleanSpan(String value){
        return value.replaceAll("^\\s+.*\\s+$", "");
    }


    public enum ClearCharType {
        ALL,
        START,
        END,
        NO_MIDDLE;
    }


    /**统计指定字符
     * 2022/9/19 0019-16:28
     * @author pengfulin
     * @param value 待统计的字符串
     * @param charValue 待统计的字符
     * @param totalCharType 统计类型
     * @return 返回指定字符在字符串中的数量
    */
    public static int totalChar(String value, char charValue, TotalCharType totalCharType){
        int count=0;
        if(totalCharType == TotalCharType.ALL){
            for (int i = 0; i < value.length(); i++) {
                if(value.charAt(i)==charValue)
                    count++;
            }
        }
        if(totalCharType == TotalCharType.START|| totalCharType == TotalCharType.NO_MIDDLE) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == charValue)
                    count++;
                else
                    break;
            }
        }
        if(totalCharType == TotalCharType.END|| totalCharType == TotalCharType.NO_MIDDLE) {
            for (int length = value.length()-1; length > 0; length--) {
                if(value.charAt(length)==charValue)
                    count++;
                else
                    break;
            }
        }
        return count;
    }

    public enum TotalCharType {
        ALL,
        START,
        END,
        NO_MIDDLE;
    }


    /**提取字符串
     * 2022/9/26 0026-16:00
     * @param value 待提取字符串值
     * @param start 开始位置
     * @param end 结束位置
     * @param isContain 是否包含开始/结束
     * @param isStartIndex  是否为从前开始，否则将从尾部开始
     * @author pengfulin
     */
    public static String substring(String value,String start,String end,boolean isContain, boolean isStartIndex){
        //如果start&&end为空
        if(StringUtils.isEmpty(start,true)&& StringUtils.isEmpty(end,true))
            return null;
        //如果start||end为空
        if(StringUtils.isEmpty(start,true)|| StringUtils.isEmpty(end,true)){
            int index;
            if(StringUtils.isEmpty(start,true)){  //开始点为null
                index=subStringIndex(end,index(value, end, isStartIndex, isContain,false));
                if(index<0) return null;
                return value.substring(0,index);
            }else{  //结束点为null
                index = subStringIndex(start,index(value, start, isStartIndex, isContain,true));
                if(index<0) return null;
                return value.substring(index);
            }
        }
        //如果start&&end都不为空
        int startIndex=subStringIndex(start,index(value, start, isStartIndex, isContain,true));
        int endIndex=subStringIndex(end,index(value, end, isStartIndex, isContain,false));
        if(startIndex<0||endIndex<0)
            return null;
        if(startIndex>endIndex)
            return null;
        return value.substring(startIndex, endIndex);
    }

    /**
     * 提取字符串：从首或尾部
     * 2023/9/20 23:28
     * @author pengshuaifeng
     */
    public static String subFromHeadOrEnd(String value,String substringValue,boolean fromHead){
        if (fromHead && value.startsWith(substringValue)) {
            return substring(value,substringValue,null,false,true);
        }else if(!fromHead && value.endsWith(substringValue)){
            return substring(value,null,substringValue,false,false);
        }
        return value;
    }


    /**文件转字符串集合
     * 2022/9/26 0026-15:43
     * @param reader 读取字符流
     * @param isClearSpacing 是否清除空格
     * @param isClearBlankLines 是否清除空行
     * @param codeBr 换行符
     * @author pengfulin
     */
    public static List<String> fileToLines(Reader reader, boolean isClearSpacing , boolean isClearBlankLines, String codeBr) {
        try {
            LinkedList<String> lines = new LinkedList<>();
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTemp;
            while ((lineTemp = bufferedReader.readLine()) != null) {
                StringBuilder builderLine = new StringBuilder();
                if (isClearSpacing)
                    lineTemp = lineTemp.trim();
                if(isClearBlankLines){
                    if (!(lineTemp.trim().length()>0))
                        continue; //去除空行
                }
                builderLine.append(lineTemp);
                if(codeBr!=null)
                    builderLine.append(codeBr);
                lines.add(builderLine.toString());
            }
            return lines.isEmpty()?null:lines;
        } catch (Exception e) {
            throw new RuntimeException("文件转化字符串集合异常",e);
        }
    }

    /**文件转字符串
     * 2022/10/10 0010-14:30
     * @author pengfulin
    */
    public static String fileToString(Reader reader, boolean isClearSpacing , boolean isClearBlankLines, String codeBr){
        List<String> fileToLines = fileToLines(reader, isClearSpacing, isClearBlankLines, codeBr);
        if(CollectionUtils.isEmpty(fileToLines))
            return null;
        StringBuilder builder = new StringBuilder();
        for (String fileToLine : fileToLines)
            builder.append(fileToLine);
        return builderToString(builder);
    }

    /**
     * 字符串集合转字符串
     * 2023/9/5 21:15
     * @author pengshuaifeng
     */
    public static String listToString(List<String> fileToLines){
        if(CollectionUtils.isEmpty(fileToLines))
            return null;
        StringBuilder builder = new StringBuilder();
        for (String fileToLine : fileToLines)
            builder.append(fileToLine);
        return builderToString(builder);
    }

    /**
     * StringBuilder转换String
     * 2023/9/17 22:02
     * @author pengshuaifeng
     */
    public static String builderToString(StringBuilder builder){
        return builder.length()==0?null:builder.toString();
    }



    /**
     * substring提取的索引自动转换
     * 2023/11/1 0001 17:15
     * @author fulin-peng
     */
    private static int subStringIndex(String value,int startIndex){
        return startIndex>=0 && value.length()!=1?startIndex+value.length()-1:startIndex;
    }


    /**获取指定字符串的索引位置
     * 2022/9/26 0026-16:14
     * @author pengfulin
     * @param value 源字符串
     * @param indexValue 指定字符串
     * @param isStartIndex 是否为从前开始，否则将从尾部开始
     * @param isContain 是否为包含位置：便于截取操作
     * @param isPrefix 指定字符串是否在前缀:便于截取操作
     * @return 返回指定字符串位置的索引
     */
    private static int index(String value,String indexValue,boolean isStartIndex,boolean isContain, boolean isPrefix){
        int index;
        if(isStartIndex){
            index=value.indexOf(indexValue);
        }else{
            index=value.lastIndexOf(indexValue);
        }
        if(index<0) return index;
        if(isPrefix)
            index=isContain?index:index+1;
        else
            index=isContain?index+1:index;
        return index;
    }
}