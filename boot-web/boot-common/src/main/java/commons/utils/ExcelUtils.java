package commons.utils;

import commons.model.annotations.Dict;
import io.swagger.annotations.ApiModelProperty;
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
     * @param headers 工作表表头关联映射（key：表头字段，value：T对象字段名）为空则会按照默认规则自动生成：generateHeaderMappings()
     * @param startReadRowAt 读取表开始行索引
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    public static <T> Map<String,Object> read(InputStream in, Class<T> targetType, Map<String,String> headers, int startReadRowAt)  {
        return read(in, targetType, null, headers, 0, startReadRowAt);
    }

    /**
     * 读取excel
     * <p>表头默认为第一行,从第二行第一列开始读取</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param headers 工作表表头关联映射（key：表头字段，value：T对象字段名）为空则会按照默认规则自动生成：generateHeaderMappings()
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    public static <T> Map<String,Object> read(InputStream in,Class<T> targetType,Map<String,String> headers)  {
        return read(in, targetType, null, headers, 0, 1);
    }

    /**
     * 读取excel
     * <p>表头默认为第一行</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param sheetNames 读取的工作表名称集合，为null，则会读取所有的表
     * @param headers 工作表表头关联映射（key：表头字段，value：T对象字段名）为空则会按照默认规则自动生成：generateHeaderMappings()
     * @param startReadRowAt 读取表开始行索引
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    public static <T> Map<String,Object> read(InputStream in,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers,int startReadRowAt) {
        return read(in, targetType,sheetNames, headers, 0, startReadRowAt);
    }

    /**
     * 读取excel
     * <p>表头默认为第一行,从第二行第一列开始读取</p>
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param sheetNames 读取的工作表名称集合，为null，则会读取所有的表
     * @param headers 工作表表头关联映射（key：表头字段，value：T对象字段名）为空则会按照默认规则自动生成：generateHeaderMappings()
     * 2024/1/4 22:23
     * @author pengshuaifeng
     */
    //TODO 表头映射应该和sheet一一绑定而不是共享表头
    public static <T> Map<String,Object> read(InputStream in,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers)  {
        return read(in, targetType, sheetNames, headers, 0, 1);
    }

    /**
     * 读取excel
     * @param in excel输入流
     * @param targetType 读取实体对象
     * @param sheetNames 读取的工作表名称集合，为null，则会读取所有的表
     * @param headers 工作表表头关联映射（key：表头字段，value：T对象字段名）为空则会按照默认规则自动生成：generateHeaderMappings()
     * @param headerAt 工作表表头所在行索引
     * @param startReadRowAt 读取表开始行索引
     * 2024/1/4 01:16
     * @author pengshuaifeng
     */
    public static <T> Map<String,Object> read(InputStream in,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers,int headerAt,int startReadRowAt){
        Workbook workbook = getWorkBook(in);
        return readWorkbook(workbook,targetType,sheetNames,headers,headerAt,startReadRowAt);
    }


    /**
     * 读取工作簿
     * 2024/1/4 23:41
     * @author pengshuaifeng
     */
    private static <T>  Map<String,Object> readWorkbook(Workbook workbook,Class<T> targetType,Collection<String> sheetNames,Map<String,String> headers,int headerAt,int startReadRowAt){
        try {
            //读取结果集
            Map<String,Object> results=new LinkedHashMap<>();
            if(sheetNames==null){ //遍历读取所有工作表
                for (Sheet sheet : workbook) {
                    readWorkbookWExecute(sheet,targetType,headers,headerAt,startReadRowAt,results);
                }
            }else{   //读取指定工作表
                for (String sheetName : sheetNames) {
                    Sheet sheet = workbook.getSheet(sheetName);
                    readWorkbookWExecute(sheet,targetType,headers,headerAt,startReadRowAt,results);
                }
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("读取工作簿异常",e);
        }
    }

    private static <T> void  readWorkbookWExecute(Sheet sheet, Class<T> targetType, Map<String,String> headers, int headerAt, int startReadRowAt,
                                                  Map<String,Object> results){
        Map<Integer, Field> headerMappings = generateHeaderMappings(sheet, headerAt, targetType, headers);
        results.put(sheet.getSheetName(),readSheet(sheet,headerMappings,targetType,startReadRowAt));//读取工作表
    }


    /**
     * 读取工作表
     * 2024/1/4 23:41
     * @author pengshuaifeng
     */
    private static <T> Collection<T> readSheet(Sheet sheet,Map<Integer,Field> headers,Class<T> targetType, int startReadRowAt){
        try {
            Collection<T> results = new LinkedList<>();
            //生成表头映射
            for (Row row : sheet) {
                if (row.getRowNum()<startReadRowAt) //从startReadRow索引行开始读取
                    continue;
                results.add(readRow(row,headers,targetType));
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
    private static <T> T readRow(Row row, Map<Integer,Field> headers, Class<T> targetType){
        try {
            T t = targetType.newInstance();
            for (Map.Entry<Integer, Field> header : headers.entrySet()) {
                Field field = header.getValue();
                Object cellValue = readCell(row.getCell(header.getKey()), field.getType());//读取单元格数据
                ClassUtils.setFieldValue(field,cellValue,t);
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
    public static void write(OutputStream out,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers, ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) {
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
    public static byte[] writeToBytes(Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers, ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) {
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
                                      int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) {
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
                             int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress) {
        try {
            out.write(writeToBytes(model, contents, headers,headerAt,startWriteRowAt, startWriteColAt, excelType,null, null, cellRangeAddress==null?null:new ExcelCellRangeAddressModel(cellRangeAddress,null)));
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("导出excel异常",e);
        }
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
                                      int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,Collection<CellRangeAddress> cellRangeAddress)  {
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
                             int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,ExcelCellStyleModel headerStyle,ExcelCellStyleModel contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel) {
        try {
            out.write(writeToBytes(model, contents, headers,headerAt,startWriteRowAt, startWriteColAt, excelType, headerStyle, contentStyle, cellRangeAddressModel));
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("导出excel异常",e);
        }
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
    public static byte[] writeToBytes(InputStream model,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                                      int headerAt,int startWriteRowAt,int startWriteColAt,ExcelType excelType,ExcelCellStyleModel headerStyle,ExcelCellStyleModel contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel){
        Workbook workbook;
        if(model!=null){
            //关闭样式
            ExcelCellStyleModel excelCellStyleModel = new ExcelCellStyleModel();
            excelCellStyleModel.enable=false;
            headerStyle=excelCellStyleModel;
            contentStyle=excelCellStyleModel;
            workbook=getWorkBook(model);  //使用模型工作簿
        }else{
            workbook=generateWorkBook(excelType); //使用新建的工作簿
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
     * @param cellRangeAddressModel 单元格合并模型,默认使用表头样式充当合并样式
     * 2024/1/4 01:17
     * @return excel字节数组
     * @author pengshuaifeng
     */
    public static byte[] writeToBytes(Workbook workbook,Map<String,Collection<?>> contents,Map<String,Map<String,String>> headers,
                                      int headerAt,int startWriteRowAt,int startWriteColAt,ExcelCellStyleModel headerStyle,ExcelCellStyleModel contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel){
        try {
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
                    Field declaredField = ClassUtils.getField(targetType,headerField.getValue());
                    declaredField.setAccessible(true);
                    headerToFields.put(headerField.getKey(),declaredField);
                }
                writeSheet(sheet,value,headerToFields,headerAt,startWriteRowAt,startWriteColAt,headerStyle,contentStyle,cellRangeAddressModel);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("导出excel异常",e);
        }
    }



    /**
     * 写入sheet
     * 2024/1/7 00:16
     * @author pengshuaifeng
     */
    private static void writeSheet(Sheet sheet,Collection<?> rows,Map<String,Field> headers, int headerAt,int startWriteRowAt,int startWriteColAt,
                                   ExcelCellStyleModel headerStyle,ExcelCellStyleModel contentStyle,ExcelCellRangeAddressModel cellRangeAddressModel){
        try {
            headerStyle=getHeaderStyle(headerStyle);
            contentStyle=getContentStyle(contentStyle);
            CellStyle headerCellStyle = generateCellStyle(headerStyle, sheet.getWorkbook());
            CellStyle contentCellStyle = generateCellStyle(contentStyle, sheet.getWorkbook());
            if (cellRangeAddressModel!=null &&cellRangeAddressModel.cellStyleModel==null) {
                cellRangeAddressModel.setCellStyle(headerCellStyle);
            }
            if(headerStyle.enable){
                //TODO 后续可以更精细设置列宽：例如根据字段哪些自动列宽、哪些固定列宽
                if(headerStyle.columnWidth==null){
                    //设置自动列宽
                    for (int i = 0; i < headers.size(); i++) {
                        sheet.autoSizeColumn(i);
                    }
                }else{
                    //设置固定列宽
                    for (int i = 0; i < headers.size(); i++) {
                        sheet.setColumnWidth(i,headerStyle.columnWidth);
                    }
                }
            }
            if(headerAt!=-1){  //表头数据写入
                Row headerRows = sheet.createRow(headerAt);
                writeRow(headerRows,headers.keySet(),null,startWriteColAt,headerCellStyle);
            }
            //内容数据写入
            Iterator<?> iterator = rows.iterator();
            for (int i = 0; i < rows.size(); i++) {
                writeRow(sheet.createRow(i+startWriteRowAt),iterator.next(),headers.values(),startWriteColAt,contentCellStyle);
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
            if(cellStyle!=null)
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
    private static Map<Integer,Field> generateHeaderMappings(Sheet sheet,int index,Class<?> source,Map<String,String> headerSource)  {
        Map<Integer, Field> headerMappings = new LinkedHashMap<>();
        Row row = sheet.getRow(index);
        if(CollectionUtils.isNotEmpty(headerSource)){
            for (Cell cell : row) {
                int columnIndex = cell.getColumnIndex();
                String header = cell.getStringCellValue();
                Field declaredField = ClassUtils.getField(source,headerSource.get(header));
                //处理字典类型，优先根据Dict注解中文匹配，同时如果有标注对应的nameEn，则会将此列的值赋予nameEn所关联的字段
                Dict dict = declaredField.getAnnotation(Dict.class);
                if(dict!=null&& (header.equals(dict.nameCn())|| header.equals(dict.nameEn()))) {
                    if (StringUtils.isNotEmpty(dict.nameEn())) {
                        headerMappings.put(columnIndex, ClassUtils.getField(source, dict.nameEn()));
                    } else {
                        headerMappings.put(columnIndex, declaredField);
                    }
                }else{
                    headerMappings.put(columnIndex, declaredField);
                }
            }
        }else{
            headerSource= CollectionUtils.isNullOrDefault(headerSource);
            for (Field declaredField : source.getDeclaredFields()) {
                for (Cell cell : row) {
                    boolean matched=false;
                    int columnIndex = cell.getColumnIndex();
                    String header = cell.getStringCellValue();
                    //处理字典类型，优先根据Dict注解中文匹配，同时如果有标注对应的nameEn，则会将此列的值赋予nameEn所关联的字段
                    Dict dict = declaredField.getAnnotation(Dict.class);
                    if(dict!=null&& (header.equals(dict.nameCn())|| header.equals(dict.nameEn()))){
                        if(StringUtils.isNotEmpty(dict.nameEn())){
                            headerMappings.put(columnIndex,ClassUtils.getField(source,dict.nameEn()));
                        }else{
                            headerMappings.put(columnIndex, declaredField);
                        }
                        matched=true;
                    }else{
                        ApiModelProperty apiModelProperty = declaredField.getAnnotation(ApiModelProperty.class);
                        if (apiModelProperty!=null && apiModelProperty.value().equals(header)) {
                            headerMappings.put(columnIndex, declaredField);
                            matched=true;
                        }else if (declaredField.getName().equals(header)) {
                            headerMappings.put(columnIndex,declaredField);
                            matched=true;
                        }
                    }
                    if(matched){
                        headerSource.put(header,declaredField.getName());
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
    public static Map<String,String> generateHeaders(Class<?> source,Collection<String> selectedFields,Collection<String> ignoreFields){
        Map<String, String> headers = new LinkedHashMap<>();
        if(selectedFields!=null){
            for (String fieldName : selectedFields) {
                Field field = ClassUtils.getField(source,fieldName);
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
        String fieldName = field.getName();
        String headName = fieldName;
        Dict dict = field.getAnnotation(Dict.class);
        if(dict!=null && StringUtils.isNotEmpty(dict.nameCn())){
            headName= dict.nameCn();
        }else{
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            if(apiModelProperty!=null){
                headName=apiModelProperty.value();
            }
        }
        headers.put(headName,fieldName);
    }


    /**
     * 获取表头样式模型
     * 2024/1/6 21:01
     * @author pengshuaifeng
     */
    private static ExcelCellStyleModel getHeaderStyle(ExcelCellStyleModel cellStyle){
        return cellStyle==null? ExcelCellStyleModel.DEFAULT_HEADER_STYLE:cellStyle;
    }

    /**
     * 获取数据行模型
     * 2024/1/6 21:01
     * @author pengshuaifeng
     */
    private static ExcelCellStyleModel getContentStyle(ExcelCellStyleModel cellStyle){
        return cellStyle==null? ExcelCellStyleModel.DEFAULT_CONTENT_STYLE:cellStyle;
    }

    /**
     * 生成样式
     * 2024/8/30 22:34
     * @author pengshuaifeng
     */
    private static CellStyle generateCellStyle(ExcelCellStyleModel cellStyleModel,Workbook workbook){
        if(!cellStyleModel.enable)
            return null;
        CellStyle cellStyle = workbook.createCellStyle();
        setAlign(cellStyle, cellStyleModel.getHorizontalAlignment(), cellStyleModel.getVerticalAlignment()); //设置对齐
        setBorder(cellStyle, cellStyleModel.getBorderStyle(), cellStyleModel.getBorderColor()); //设置边框
        setBackgroundFill(cellStyle, cellStyleModel.getBackgroundColor(), cellStyleModel.getFillPatternType()); //设置背景填充
        setFont(cellStyle, workbook.createFont(), cellStyleModel.fontCellModel.getFontName(),
                cellStyleModel.fontCellModel.getFontColor(), cellStyleModel.fontCellModel.getBold(), cellStyleModel.fontCellModel.getHeightInPoints()); //设置字体
        cellStyle.setWrapText(true); //自动换行
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
        if (horizontal!=null)
            cellStyle.setAlignment(horizontal);
        if (vertical!=null)
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
        if (borderStyle!=null){
            cellStyle.setBorderBottom(borderStyle);
            cellStyle.setBorderLeft(borderStyle);
            cellStyle.setBorderRight(borderStyle);
            cellStyle.setBorderTop(borderStyle);
        }
        if (indexedColors!=null){
            cellStyle.setBottomBorderColor(indexedColors.index);
            cellStyle.setLeftBorderColor(indexedColors.index);
            cellStyle.setRightBorderColor(indexedColors.index);
            cellStyle.setTopBorderColor(indexedColors.index);
        }
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
        if (indexedColors!=null)
            cellStyle.setFillForegroundColor(indexedColors.index);
        if (fillPattern!=null)
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
    private static void setFont(CellStyle cellStyle,Font font,String fontName, IndexedColors indexedColors,Boolean bold,Short heightInPoints){
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
        CellStyle cellStyle =cellRangeAddressModel.cellStyle!=null?
                cellRangeAddressModel.cellStyle: cellRangeAddressModel.cellStyleModel != null ? generateCellStyle(cellRangeAddressModel.getCellStyleModel(), sheet.getWorkbook()) : null;
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
                    cell.setCellStyle(cellStyle);
                }
            }
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    /**
     * 获取结果集
     * 2024/8/26 下午6:00
     * @author fulin-peng
     */
    public static <T> Collection<T> getDefaultResultCollection(Map<String,Object> result){
        return getResultCollection(result, "Sheet1");
    }

    public static <T> Collection<T> getDefaultResultCollection(Map<String,Object> result,int at){
        return getResultCollection(result,"Sheet"+at);
    }

    public static <T> Collection<T> getResultCollection(Map<String,Object> result,String sheetName){
        if (result==null)
            return null;
        return (Collection<T>) result.get(sheetName);
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
    public static class ExcelCellRangeAddressModel {
        public ExcelCellRangeAddressModel(Collection<CellRangeAddress> cellRangeAddress, ExcelCellStyleModel cellStyleModel) {
            this.cellRangeAddress = cellRangeAddress;
            this.cellStyleModel = cellStyleModel;
        }
        //合并单元格集合
        private Collection<CellRangeAddress> cellRangeAddress;
        //合并单元格样式模型
        private ExcelCellStyleModel cellStyleModel;
        //合并单元格样式
        private CellStyle cellStyle;
    }

    /**
     * excel单元格样式模型
     */
    @Data
    public static class  ExcelCellStyleModel{
        /**
         * 适用于自定义配置样式下，统一配置，不用每次都传入
         */
        public static final   ExcelCellStyleModel DEFAULT_HEADER_STYLE = defaultHeaderCellStyle();

        public static final ExcelCellStyleModel DEFAULT_CONTENT_STYLE = defaultContentCellStyle();

        //是否开启
        private boolean enable=true;

        //1.字体
        private FontCellModel fontCellModel;
        //2.背景
        //填充
        private FillPatternType fillPatternType;
        //颜色
        private IndexedColors backgroundColor;
        //3.边框
        //边框样式
        private BorderStyle borderStyle;
        //边框颜色
        private IndexedColors borderColor;
        //4.对齐
        //水平对齐
        private HorizontalAlignment horizontalAlignment;
        //垂直对齐
        private VerticalAlignment verticalAlignment;
        //5.列宽度 单位：字符-1/256个字符宽度
        private Integer columnWidth;
        //自动换行
        private Boolean wrapText=true;
        @Data
        public static class  FontCellModel{
            //字体名称
            private String fontName;
            //字体颜色
            private IndexedColors fontColor;
            //字体大小
            private Short heightInPoints;
            //字体加粗
            private Boolean bold;
        }
    }


    /**
     * 默认表头样式
     * 2024/8/30 22:24
     * @author pengshuaifeng
     */
    public static ExcelCellStyleModel defaultHeaderCellStyle(){
        ExcelCellStyleModel excelCellStyleModel = new ExcelCellStyleModel();
        //字体样式
        ExcelCellStyleModel.FontCellModel fontCellModel = new ExcelCellStyleModel.FontCellModel();
        fontCellModel.setBold(true);
        fontCellModel.setFontName("宋体");
        excelCellStyleModel.setFontCellModel(fontCellModel);
        //背景样式
        excelCellStyleModel.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        excelCellStyleModel.setBackgroundColor(IndexedColors.GREY_25_PERCENT);
        //边框样式
        excelCellStyleModel.setBorderStyle(BorderStyle.THIN);
        excelCellStyleModel.setBorderColor(IndexedColors.BLACK);
        //对齐样式
        excelCellStyleModel.setHorizontalAlignment(HorizontalAlignment.CENTER);
        excelCellStyleModel.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行
        excelCellStyleModel.setWrapText(true);
        //列宽度
        excelCellStyleModel.setColumnWidth(4000);
        return excelCellStyleModel;
    }

    /**
     * 默认内容样式
     * 2024/8/30 22:23
     * @author pengshuaifeng
     */
    public static ExcelCellStyleModel defaultContentCellStyle(){
        ExcelCellStyleModel excelCellStyleModel = new ExcelCellStyleModel();
        //字体样式
        ExcelCellStyleModel.FontCellModel fontCellModel = new ExcelCellStyleModel.FontCellModel();
        fontCellModel.setFontName("宋体");
        excelCellStyleModel.setFontCellModel(fontCellModel);
        //边框样式
        excelCellStyleModel.setBorderStyle(BorderStyle.THIN);
        excelCellStyleModel.setBorderColor(IndexedColors.BLACK);
        //对齐样式
        excelCellStyleModel.setHorizontalAlignment(HorizontalAlignment.CENTER);
        excelCellStyleModel.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行
        excelCellStyleModel.setWrapText(true);
        return excelCellStyleModel;
    }
}
