package commons.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * excel工具类
 *
 * @author pengshuaifeng
 * 2024/1/4
 */
public class ExcelUtils {

    /**
     * 读取excel
     * <p>表头默认为第一行</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param headers 工作表表头关联映射，为空则会按照默认规则自动生成
     * @param startReadRowAt 读取表开始行索引
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    public static <T> Map<String,Collection<T>> read(InputStream in, Class<T> targetType, Map<String,String> headers, int startReadRowAt) throws Exception {
        return read(in, targetType, null, headers, 0, startReadRowAt);
    }

    /**
     * 读取excel
     * <p>表头默认为第一行,从第二行第一列开始读取</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param headers 工作表表头关联映射，为空则会按照默认规则自动生成
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    public static <T> Map<String,Collection<T>> read(InputStream in,Class<T> targetType,Map<String,String> headers) throws Exception {
        return read(in, targetType, null, headers, 0, 1);
    }

    /**
     * 读取excel
     * <p>表头默认为第一行</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param sheetNames 读取的工作表名称集合，为null，则会读取所有的表
     * @param headers 工作表表头关联映射，为空则会按照默认规则自动生成
     * @param startReadRowAt 读取表开始行索引
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    public static <T> Map<String,Collection<T>> read(InputStream in,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers,int startReadRowAt) throws Exception {
        return read(in, targetType,sheetNames, headers, 0, startReadRowAt);
    }

    /**
     * 读取excel
     * <p>表头默认为第一行,从第二行第一列开始读取</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param sheetNames 读取的工作表名称集合，为null，则会读取所有的表
     * @param headers 工作表表头关联映射，为空则会按照默认规则自动生成
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    //TODO 表头映射应该和sheet一一绑定而不是共享表头
    public static <T> Map<String,Collection<T>> read(InputStream in,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers) throws Exception {
        return read(in, targetType, sheetNames, headers, 0, 1);
    }

    /**
     * 读取excel
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param sheetNames 读取的工作表名称集合，为null，则会读取所有的表
     * @param headers 工作表表头关联映射，为空则会按照默认规则自动生成
     * @param headerAt 工作表表头所在行索引
     * @param startReadRowAt 读取表开始行索引
     * 2024/1/4 01:16
     * @author pengshuaifeng
     */
    public static <T> Map<String,Collection<T>> read(InputStream in,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers,int headerAt,int startReadRowAt)throws Exception{
        Workbook workbook = getWorkBook(in);
        return readWorkbook(workbook,targetType,sheetNames,headers,headerAt,startReadRowAt);
    }


    /**
     * 读取工作簿
     * 2024/1/4 23:41
     * @author pengshuaifeng
     */
    private static  <T> Map<String,Collection<T>> readWorkbook(Workbook workbook,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers,int headerAt,int startReadRowAt){
        try {
            //读取结果集
            Map<String,Collection<T>> results=new LinkedHashMap<>();
            if(sheetNames==null){ //遍历读取所有工作表
                for (Sheet sheet : workbook) {
                    results.put(sheet.getSheetName(),readSheet(sheet,headers,headerAt,targetType,startReadRowAt));//读取工作表
                }
            }else{   //读取指定工作表
                for (String sheetName : sheetNames) {
                    Sheet sheet = workbook.getSheet(sheetName);
                    results.put(sheet.getSheetName(),readSheet(sheet,headers,headerAt,targetType,startReadRowAt));//读取工作表
                }
            }
            return results;
        } catch (Exception e) {
           throw new RuntimeException("读取工作簿异常",e);
        }
    }

    /**
     * 读取工作表
     * 2024/1/4 23:41
     * @author pengshuaifeng
     */
    private static  <T> Collection<T> readSheet(Sheet sheet,Map<String,String> headers,int headerAt,Class<T> targetType, int startReadRowAt){
        try {
            Collection<T> results = new LinkedList<>();
            //生成表头映射
            Map<Integer,Field> headersTemp;
            headersTemp=generateHeaderMappings(sheet,headerAt,targetType,headers);
            for (Row row : sheet) {
                if (row.getRowNum()<startReadRowAt) //从startReadRow索引行开始读取
                    continue;
                results.add(readRow(row,headersTemp,targetType));
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("读取工作表异常",e);
        }
    }

    /**
     * 读取数据行
     * 2024/1/4 23:40
     * @author pengshuaifeng
     */
    private static  <T> T readRow(Row row, Map<Integer,Field> headers, Class<T> targetType){
        try {
            T t = targetType.newInstance();
            for (Map.Entry<Integer, Field> header : headers.entrySet()) {
                Field field = header.getValue();
                field.set(t,readCell(row.getCell(header.getKey()),field.getType()));  //读取单元格数据
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException("读取数据行异常",e);
        }
    }


    /**
     * 读取单元格
     * 2024/1/4 23:35
     * @author pengshuaifeng
     * @param cell 单元格对象
     * @param targetType 目标数据类型
     */
    //TODO 数字类型读取待完善
    private static Object readCell(Cell cell, Class<?> targetType){
        switch (cell.getCellType()) {
            case STRING:  //字符类型
                return cell.getStringCellValue();
            case NUMERIC:  //数字类型
                if (DateUtil.isCellDateFormatted(cell)) {  //时间类型处理
                    if (targetType.isAssignableFrom(LocalDateTime.class)) {
                        return cell.getLocalDateTimeCellValue();
                    }
                    return cell.getDateCellValue();
                } else {
                    double numericCellValue = cell.getNumericCellValue();
                    if (targetType.isAssignableFrom(int.class) || targetType.isAssignableFrom(Integer.class) ) {
                        //int类型处理
                        return  (int)numericCellValue;
                    }else{
                        //其他类型处理
                        return numericCellValue;
                    }
                }
            case BOOLEAN:  //布尔类型
                return cell.getBooleanCellValue();
            case FORMULA:  //公式类型：转换成字符表达式
                return cell.getCellFormula();
            case BLANK:  //空白类型
                if(targetType.isAssignableFrom(Number.class)||targetType.isAssignableFrom(int.class)){
                    return 0;  //数字类型处理
                }else{  //其他类型处理
                    return null;
                }
            default:
                throw new RuntimeException("单元格<"+cell.getRowIndex()+"/"+cell.getColumnIndex()+">未知的数据类型："+cell.getCellType());
        }
    }


    /**
     * 导出表格
     * <p>使用默认样式,默认第一行为表头，第二行，第一列开始输出数据</p>
     * @param out excel输出流
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param excelType excel类型
     * @param cellRangeAddress 合并单元格集
     * 2024/1/7 11:06
     * @author pengshuaifeng
     */
    public static void write(OutputStream out,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers, ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) throws Exception{
        write(out,null,contents, headers,0,1, 0, excelType, cellRangeAddress);
    }

    /**
     * 导出表格
     * <p>使用默认样式,默认第一行为表头，第二行，第一列开始输出数据</p>
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param excelType excel类型
     * @param cellRangeAddress 合并单元格集
     * 2024/1/7 11:06
     * @return excel字节数组
     * @author pengshuaifeng
     */
    public static byte[] writeToBytes(Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers, ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) throws Exception{
        return writeToBytes( contents, headers, 0, 1, 0, excelType, cellRangeAddress);
    }

    /**
     * 导出表格
     * <p>使用默认样式</p>
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param headerAt 导出表头行索引，为-1不生成表头
     * @param startWriteRowAt 导出起始行索引
     * @param startWriteColAt 导出起始列索引
     * @param excelType excel类型，为空excelType必
     * @param cellRangeAddress 合并单元格集
     * 2024/1/7 11:06
     * @return excel字节数组
     * @author pengshuaifeng
     */
    public static byte[] writeToBytes(Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                                      int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) throws Exception{
        return writeToBytes(null, contents, headers,headerAt,startWriteRowAt, startWriteColAt, excelType,null, null,
                cellRangeAddress==null?null:new ExcelCellRangeAddressModel(cellRangeAddress,null));
    }




    /**
     * 导出表格
     * <p>使用默认样式</p>
     * @param out excel输出流
     * @param model excel模版输入流，为空excelType必填
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param headerAt 导出表头行索引，为-1不生成表头
     * @param startWriteRowAt 导出起始行索引
     * @param startWriteColAt 导出起始列索引
     * @param excelType excel类型
     * @param cellRangeAddress 合并单元格集
     * 2024/1/7 11:06
     * @author pengshuaifeng
     */
    public static void write(OutputStream out,InputStream model,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                             int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) throws Exception{
        out.write(writeToBytes(model, contents, headers,headerAt,startWriteRowAt, startWriteColAt, excelType,null, null, cellRangeAddress==null?null:new ExcelCellRangeAddressModel(cellRangeAddress,null)));
        out.close();
    }

    /**
     * 导出表格
     * <p>使用默认样式</p>
     * @param model excel模版输入流
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param headerAt 导出表头行索引，为-1不生成表头
     * @param startWriteRowAt 导出起始行索引
     * @param startWriteColAt 导出起始列索引
     * @param excelType excel类型，为空excelType必
     * @param cellRangeAddress 合并单元格集
     * 2024/1/7 11:06
     * @return excel字节数组
     * @author pengshuaifeng
     */
    public static byte[] writeToBytes(InputStream model,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                             int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) throws Exception{
        return writeToBytes(model, contents, headers,headerAt,startWriteRowAt, startWriteColAt, excelType,null, null,
                cellRangeAddress==null?null:new ExcelCellRangeAddressModel(cellRangeAddress,null));
    }


    /**
     * 导出excel
     * @param out excel输出流
     * @param model excel模版输入流
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param headerAt 导出表头行索引，为-1不生成表头
     * @param startWriteRowAt 导出起始行索引
     * @param startWriteColAt 导出起始列索引
     * @param excelType excel类型，为空excelType必
     * @param headerStyle 导出表头样式,为空使用默认表头样式
     * @param contentStyle 导出内容样式，为空使用默认内容样式
     * @param cellRangeAddressModel 单元格合并模型,默认使用表头样式充当合并样式
     * 2024/1/4 01:17
     * @author pengshuaifeng
     */
    public static void write(OutputStream out,InputStream model,Map<String,Collection<?>> contents,Map<String,Map<String,String>>headers,
                             int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,CellStyle headerStyle,CellStyle contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel) throws Exception{
        out.write(writeToBytes(model, contents, headers,headerAt,startWriteRowAt, startWriteColAt, excelType, headerStyle, contentStyle, cellRangeAddressModel));
        out.close();
    }


    /**
     * 导出excel
     * @param model excel模版输入流
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param headerAt 导出表头行索引，为-1不生成表头
     * @param startWriteRowAt 导出起始行索引
     * @param startWriteColAt 导出起始列索引
     * @param excelType excel类型，为空excelType必
     * @param headerStyle 导出表头样式,为空使用默认表头样式
     * @param contentStyle 导出内容样式，为空使用默认内容样式
     * @param cellRangeAddressModel 单元格合并模型,默认使用表头样式充当合并样式
     * 2024/1/4 01:17
     * @return excel字节数组
     * @author pengshuaifeng
     */
    //TODO 后续可加入自定义样式配置对象，暂只使用默认的通用样式
    public static byte[] writeToBytes(InputStream model,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                                      int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,CellStyle headerStyle,CellStyle contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel)throws Exception{
        Workbook workbook;
        if(model!=null){
            workbook=getWorkBook(model);  //使用模型工作簿
        }else{
            workbook=generateWorkBook(excelType); //使用新建的工作簿
        }
        headerStyle = getHeaderCellStyle(headerStyle, workbook);
        contentStyle = getContentStyle(contentStyle, workbook);
        if (cellRangeAddressModel!=null) {
            cellRangeAddressModel.setCellStyle(headerStyle);
        }
        return writeToBytes(workbook,contents,headers,headerAt,startWriteRowAt,startWriteColAt, headerStyle, contentStyle,cellRangeAddressModel);
    }



    /**
     * 导出excel
     * @param contents 导出数据集，key为工作表名，value为数据集
     * @param headers 导出表头映射集，为空自动生成默认表头，key为工作表名，value为表头名和字段集映射集
     * @param headerAt 导出表头行索引，为-1不生成表头
     * @param startWriteRowAt 导出起始行索引
     * @param startWriteColAt 导出起始列索引
     * @param headerStyle 导出表头样式
     * @param contentStyle 导出内容样式
     * @param cellRangeAddressModel 单元格合并模型
     * 2024/1/4 01:17
     * @return excel字节数组
     * @author pengshuaifeng
     */
    public static byte[] writeToBytes(Workbook workbook,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                                      int headerAt,int startWriteRowAt,int startWriteColAt,CellStyle headerStyle,CellStyle contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel)throws Exception{
        for (Map.Entry<String, Collection<?>> content : contents.entrySet()) {
            String key = content.getKey();
            Collection<?> value = content.getValue();
            Class<?> targetType = value.stream().findFirst().get().getClass();
            Sheet sheet = workbook.getSheet(key);
            if(sheet==null){
                sheet= workbook.createSheet(key);
            }
            Map<String,String> headerToStrings;
            if (headers==null|| headers.get(key)==null) { //没有自定义表头，则创建默认表头
                try {
                    headerToStrings=generateHeaders(targetType,null,null);
                } catch (Exception e) {
                    throw new RuntimeException("生成表头异常",e);
                }
            }else{
                headerToStrings=headers.get(key); //使用自定义表头
            }
            Map<String,Field> headerToFields=new LinkedHashMap<>();
            for (Map.Entry<String, String> headerField : headerToStrings.entrySet()) {
                Field declaredField = targetType.getDeclaredField(headerField.getValue());
                declaredField.setAccessible(true);
                headerToFields.put(headerField.getKey(),declaredField);
            }
            writeSheet(sheet,value,headerToFields,headerAt,startWriteRowAt,startWriteColAt,headerStyle,contentStyle,cellRangeAddressModel);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    /**
     * 写入sheet
     * 2024/1/7 00:16
     * @author pengshuaifeng
     */
    private static void writeSheet(Sheet sheet,Collection<?> rows,Map<String,Field> headers, int headerAt,int startWriteRowAt,int startWriteColAt,CellStyle headerStyle,CellStyle contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel){
        try {
            if(headerAt!=-1){  //表头数据写入
                Row headerRows = sheet.createRow(headerAt);
                writeRow(headerRows,headers.keySet(),null,startWriteColAt,headerStyle);
            }
            //内容数据写入
            Iterator<?> iterator = rows.iterator();
            for (int i = 0; i < rows.size(); i++) {
                writeRow(sheet.createRow(i+startWriteRowAt),iterator.next(),headers.values(),startWriteColAt,contentStyle);
            }
            //合并单元格
            if(cellRangeAddressModel!=null){
                handleMergedRegion(sheet,cellRangeAddressModel);
            }
        } catch (Exception e) {
            throw new RuntimeException("写入工作表异常",e);
        }
    }

    /**
     * 写入行数据
     * 2024/1/7 00:16
     * @author pengshuaifeng
     */
    private static void writeRow(Row row,Object rowData,Collection<Field> headers,int startWriteColAt,CellStyle cellStyle){
        try {
            if(rowData instanceof Collection){ //如果是数据集合，则按照数据集合顺序依次写入集合元素
                Collection<?> data = (Collection<?>) rowData;
                Iterator<?> iterator = data.iterator();
                for (int i = 0; i < data.size(); i++) {
                    Cell cell = row.createCell(i + startWriteColAt);
                    writeCell(cell,iterator.next(),cellStyle);
                }
            }else{  //按照表头字段映射，依次获取数据对象的字段属性写入元素
                Iterator<Field> iterator = headers.iterator();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.createCell(i + startWriteColAt);
                    Field field = iterator.next();
                    writeCell(cell,field.get(rowData),cellStyle);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("写入数据行异常",e);
        }
    }

    /**
     * 写入单元格
     * 2024/1/7 00:17
     * @author pengshuaifeng
     */
    //TODO 存在数据类型格式的样式设置问题：例如时间类型
    private static void writeCell(Cell cell,Object data,CellStyle cellStyle){
        try {
            if(data ==null){
                cell.setCellValue("");
            }
            else if(data instanceof Number){
                cell.setCellValue(data instanceof Float ? Double.parseDouble(data.toString()) : ((Number)data).doubleValue());
            }else if(data instanceof Date){
                cell.setCellValue((Date)data);
            }else if(data instanceof LocalDate){
                cell.setCellValue((LocalDate)data);
            }else if(data instanceof LocalDateTime){
                cell.setCellValue((LocalDateTime)data);
            }else if(data instanceof Boolean){
                cell.setCellValue((Boolean)data);
            } else {
                cell.setCellValue(data.toString());
            }
            cell.setCellStyle(cellStyle);
        } catch (Exception e) {
            throw new RuntimeException("写入单元格异常",e);
        }
    }


    /**
     * 获取工作簿
     * @param in excel输入流
     * 2024/1/4 20:50
     * @author pengshuaifeng
     */
    private static Workbook getWorkBook(InputStream in){
        try {
            return WorkbookFactory.create(in);
        } catch (IOException e) {
            throw new RuntimeException("打开工作簿异常",e);
        }
    }

    /**
     * 生成工作簿
     * @param excelType excel类型
     * 2024/1/4 20:50
     * @author pengshuaifeng
     */
    private static Workbook generateWorkBook(ExcelType excelType){
        return excelType== ExcelType.XLS?new HSSFWorkbookFactory().create():
                new XSSFWorkbookFactory().create();
    }



    /**
     * 生成表头映射
     * @param sheet 工作表
     * @param index 表头所在的行
     * @param source 表头映射源:自动映射
     * @param headerSource 表头映射源：自定义映射（优先）
     * 2024/1/4 21:37
     * @author pengshuaifeng
     */
    private static Map<Integer,Field> generateHeaderMappings(Sheet sheet,int index,Class<?> source,Map<String,String> headerSource) throws NoSuchFieldException {
        Map<Integer, Field> headerMappings = new LinkedHashMap<>();
        Row row = sheet.getRow(index);
        if(headerSource!=null){
            for (Cell cell : row) {
                int columnIndex = cell.getColumnIndex();
                String header = cell.getStringCellValue();
                Field declaredField = source.getDeclaredField(headerSource.get(header));
                declaredField.setAccessible(true);
                headerMappings.put(columnIndex, declaredField);
            }
        }else{
            //TODO 默认只支持指定表头行为属性名的，后续可根据属性的注解获取符合表头标识符的进行映射
            for (Field declaredField : source.getDeclaredFields()) {
                for (Cell cell : row) {
                    int columnIndex = cell.getColumnIndex();
                    String header = cell.getStringCellValue();
                    if (declaredField.getName().equals(header)) {
                        declaredField.setAccessible(true);
                        headerMappings.put(columnIndex,declaredField);
                        break;
                    }
                }
            }
        }
        return headerMappings;
    }


    /**
     * 生成表头
     * @param source 表头映射源
     * @param selectedFields 选择的表头字段集
     * @param ignoreFields 需要忽略的字段集
     * 2024/1/6 19:12
     * @author pengshuaifeng
     */
    public static Map<String,String> generateHeaders(Class<?> source,Collection<String> selectedFields,Collection<String> ignoreFields) throws Exception{
        Map<String, String> headers = new LinkedHashMap<>();
        if(selectedFields!=null){
            for (String fieldName : selectedFields) {
                Field field = source.getDeclaredField(fieldName);
                setHeaders(source,field,headers);
            }
        }else{
            for (Field field :  source.getDeclaredFields()) {
                String fieldName = field.getName();
                if(ignoreFields!=null && ignoreFields.contains(fieldName))
                    continue;
                setHeaders(source,field,headers);
            }
        }
        return headers;
    }

    /**
     * 设置表头
     * 2024/1/7 11:05
     * @author pengshuaifeng
     */
    private static void setHeaders(Class<?> source,Field field,Map<String,String> headers){
        //TODO 可根据实际情况自主定义获取字段中文的注解，一般建议使用swagger相关
        String name = field.getName();
        headers.put(name,name);
    }


    /**
     * 获取表头样式
     * 2024/1/6 21:01
     * @author pengshuaifeng
     */
    private static CellStyle getHeaderCellStyle(CellStyle cellStyle,Workbook workbook){
        if(cellStyle==null){
            cellStyle=generateHeaderCellStyle(workbook);
        }
        return cellStyle;
    }


    /**
     * 生成表头样式
     * 2024/1/6 21:01
     * @author pengshuaifeng
     */
    private static CellStyle generateHeaderCellStyle(Workbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
        setBackgroundFill(cellStyle, IndexedColors.GREY_25_PERCENT, FillPatternType.SOLID_FOREGROUND);
        setFont(cellStyle,workbook.createFont(),null,null,true,null);
        return cellStyle;
    }

    /**
     * 获取数据行样式
     * 2024/1/6 21:01
     * @author pengshuaifeng
     */
    private static CellStyle getContentStyle(CellStyle cellStyle,Workbook workbook){
        if(cellStyle==null){
            cellStyle=generateContentStyle(workbook);
        }
        return cellStyle;
    }

    /**
     * 生成数据行样式
     * 2024/1/6 21:01
     * @author pengshuaifeng
     */
    private static CellStyle generateContentStyle(Workbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
        return cellStyle;
    }

    /**
     * 设置对齐
     * @param cellStyle 单元格样式对象
     * @param horizontal 水平对齐
     * @param vertical 垂直对齐
     * 2024/1/7 09:51
     * @author pengshuaifeng
     */
    private static void setAlign(CellStyle cellStyle, HorizontalAlignment horizontal, VerticalAlignment vertical) {
        cellStyle.setAlignment(horizontal);
        cellStyle.setVerticalAlignment(vertical);
    }

    /**
     * 设置边框
     * @param cellStyle 单元格样式对象
     * @param borderStyle 边框样式
     * @param indexedColors 边框颜色
     * 2024/1/7 09:51
     * @author pengshuaifeng
     */
    private static void setBorder(CellStyle cellStyle, BorderStyle borderStyle, IndexedColors indexedColors) {
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBottomBorderColor(indexedColors.index);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setLeftBorderColor(indexedColors.index);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setRightBorderColor(indexedColors.index);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setTopBorderColor(indexedColors.index);
    }

    /**
     * 设置背景填充
     * @param cellStyle 单元格样式对象
     * @param indexedColors 背景颜色
     * @param fillPattern 背景填充模式
     * 2024/1/7 09:52
     * @author pengshuaifeng
     */
    private static void setBackgroundFill(CellStyle cellStyle,IndexedColors indexedColors, FillPatternType fillPattern) {
        cellStyle.setFillForegroundColor(indexedColors.index);
        cellStyle.setFillPattern(fillPattern);
    }

    /**
     * 设置字体
     * @param cellStyle 单元格样式对象
     * @param font 字体样式对象
     * @param fontName 字体名称
     * @param indexedColors 字体颜色
     * @param bold 字体加粗
     * @param heightInPoints 字体大小
     * 2024/1/7 09:59
     * @author pengshuaifeng
     */
    private static void setFont(CellStyle cellStyle,Font font,String fontName,
                                     IndexedColors indexedColors,Boolean bold,Short heightInPoints){
        if (bold!=null)
            font.setBold(bold);
        if(fontName!=null)
            font.setFontName(fontName);
        if(indexedColors!=null)
            font.setColor(indexedColors.getIndex());
        if(heightInPoints!=null)
            font.setFontHeightInPoints(heightInPoints);
        cellStyle.setFont(font);
    }

    /**
     * 处理单元格合并
     * 2024/1/6 22:41
     * @author pengshuaifeng
     */
    private static void handleMergedRegion(Sheet sheet,ExcelCellRangeAddressModel cellRangeAddressModel){
        for (CellRangeAddress cellRangeAddress : cellRangeAddressModel.getCellRangeAddress()) {
            int firstRow = cellRangeAddress.getFirstRow();
            int firstColumn = cellRangeAddress.getFirstColumn();
            int lastColumn = cellRangeAddress.getLastColumn();
            int lastRow = cellRangeAddress.getLastRow();
            for (int r = firstRow; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                row=row==null?sheet.createRow(r):row;
                for (int c=firstColumn; c<=lastColumn; c++){
                    Cell cell = row.getCell(c);
                    cell=cell==null?row.createCell(c):cell;
                    cell.setCellStyle(cellRangeAddressModel.getCellStyle());
                }
            }
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    /**
     * excel类型
     */
    public enum ExcelType{
        XLS,
        XLSX
    }


    /**
     * excel合并单元格模型
     */
    @Data
    @AllArgsConstructor
    private static class ExcelCellRangeAddressModel {
        //合并单元格集合
        private Collection<CellRangeAddress> cellRangeAddress;
        //合并单元格样式
        private CellStyle cellStyle;
    }

    //TODO 待后续实现相关样式可自定义配置，需改造相关样式配置的方法
    /**
     * excel单元格样式模型
     */
}
