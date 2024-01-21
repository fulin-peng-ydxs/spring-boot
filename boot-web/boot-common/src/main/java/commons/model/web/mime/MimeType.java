package commons.model.web.mime;


/**
 * 媒体类型
 *
 * @author pengshuaifeng
 * 2024/1/21
 */
public class MimeType {

    //所有
    public static final String ALL = "*/*";
    //json
    public final static String APPLICATION_JSON = "application/json";
    //pdf
    public final static String APPLICATION_PDF = "application/pdf";
    //二进制：它通常用于表示二进制数据文件的内容类型。没有特定的数据格式或结构，它通常用于指示数据是未知的、不可解释的二进制数据。
    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";
    //excel-xls
    public static final String APPLICATION_XLS = "application/vnd.ms-excel";
    //excel-xlsx
    public static final String APPLICATION_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    /**
     * 获取MimeType类型，通过mime值
     * 2024/1/21 15:20
     * @author pengshuaifeng
     */
    //TODO 参考spring-web中的MediaType解析
    public static String getMimeType(String mimeValue){
        return null;
    }

    /**
     * 解析MimeType类型，通过文件名
     * 2024/1/21 15:19
     * @author pengshuaifeng
     */
    public static String parseMimeType(String fileName){
        return null;
    }

}
