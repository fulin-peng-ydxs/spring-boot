package commons.holder;


import commons.model.web.mime.MimeType;
import commons.utils.JsonUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * "servlet工"具类
 *
 * @author pengshuaifeng
 * 2023/12/30
 */
public class ServletHolder {

    /**
     * 获取响应对象
     * 2023/12/30 12:00
     * @author pengshuaifeng
     */
    public static HttpServletResponse getResponse(){
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取请求对象
     * 2023/12/30 12:04
     * @author pengshuaifeng
     */
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取请求url
     * 2024/6/3 23:27
     * @author pengshuaifeng
     */
    public static String getRequestUrl(){
        return getRequest().getRequestURL().toString();
    }

    /**
     * 获取请求uri
     * 2024/6/3 23:27
     * @author pengshuaifeng
     */
    public static String getRequestUri(){
        return getRequest().getRequestURI();
    }

    /**
     * 获取servlet路径
     * 2024/6/4 22:24
     * @author pengshuaifeng
     */
    public static String getServletPath(){
        return getRequest().getServletPath();
    }

    /**
     * 获取请求上下文
     * 2024/6/3 23:32
     * @author pengshuaifeng
     */
    public static String getServerContentPath(){
        return getRequest().getContextPath();
    }

    /**
     * 获取请求头信息
     * 2024/1/11 0011 10:00
     * @author fulin-peng
     */
    public static String getRequestHeader(String headName) {
        return getRequest().getHeader(headName);
    }


    /**
     * 获取请求会话
     * 2023/12/30 12:06
     * @author pengshuaifeng
     */
    public static HttpSession getSession(){
        HttpServletRequest request = getRequest();
        return request.getSession();
    }

    /**
     * 获取请求会话id
     * 2023/12/30 12:13
     * @author pengshuaifeng
     */
    public static String getSessionId(){
        return getSession().getId();
    }

    /**
     * 获取请求属性对象
     * 2023/12/30 12:03
     * @author pengshuaifeng
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取访问ip
     * 2023/12/18 23:54
     * @author pengshuaifeng
     */
    public static String getRequestIp(){
        String header = getRequestHeader("X-Forwarded-For");
        header=header==null?getRequestHeader( "X-Real-IP"):header;
        String remoteAddress = getRequest().getRemoteAddr();
        if(header==null && remoteAddress!=null){
            header= remoteAddress;
        }
        return header;
    }

    /**
     * 请求响应：文件流
     * 2023/12/30 13:18
     * @param response 请求响应对象
     * @param content 文件字节数据
     * @param fileName 文件响应名
     * @param mimeType 文件响应类型
     * @author pengshuaifeng
     */
    public static void responseToOutStream(HttpServletResponse response,byte[] content,String fileName,String mimeType){
        try {
            response=response==null?getResponse():response;
            //1.设置内容信息描述
              //1.1文件名进行Url参数格式编码
            String encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
            //2.设置内容的类型&大小
            response.setContentType(mimeType==null? MimeType.APPLICATION_OCTET_STREAM :mimeType);
            response.setHeader("Content-Length", Integer.toString(content.length));
            //2.写入内容到响应流&刷新
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("请求文件流响应异常",e);
        }
    }

    /**
     * 请求响应：文件流
     * 2023/12/30 13:18
     * @param response 请求响应对象
     * @param in 文件流
     * @param bufferSize 文件流读取缓存区大小
     * @param fileName 文件响应名
     * @param mimeType 文件响应类型
     * @author pengshuaifeng
     */
    public static void responseToOutStream(HttpServletResponse response, InputStream in,int bufferSize,String fileName, String mimeType){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[bufferSize<=0?4096:bufferSize];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            responseToOutStream(response,byteArrayOutputStream.toByteArray(),fileName,mimeType);
        } catch (Exception e) {
            throw new RuntimeException("请求文件流响应异常",e);
        }
    }

    /**
     * 请求响应：文件流
     * 2024/5/15 0015 17:50
     * @author fulin-peng
     */
    public static void responseToOutStream(byte[] content,String fileName,String mimeType){
        responseToOutStream(null,content,fileName,mimeType);
    }


    /**
     * 请求响应：文件流头设置
     * 2023/12/30 13:18
     * @param contentLength 文件字节大小
     * @param fileName 文件响应名
     * @param mimeType 文件响应类型
     * @author pengshuaifeng
     */
    public static void responseToOutStreamForSetHeader(HttpServletResponse response,Integer contentLength,String fileName,String mimeType){
        try {
            response=response==null?getResponse():response;
            //1.设置内容信息描述
            //1.1文件名进行Url参数格式编码
            String encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
            //2.设置内容的类型&大小
            response.setContentType(mimeType==null? MimeType.APPLICATION_OCTET_STREAM :mimeType);
            response.setHeader("Content-Length", Integer.toString(contentLength));
            //3.刷新响应数据
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("请求文件流响应异常",e);
        }
    }

    /**
     * 请求响应：json
     * 2023/12/30 13:20
     * @author pengshuaifeng
     */
    public static void responseToJson(Object obj,HttpServletResponse response){
        try {
            String responseJson = JsonUtils.getString(obj);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(responseJson);
        } catch (Exception e) {
            throw new RuntimeException("请求Json响应异常",e);
        }
    }

    public static void responseToJson(Object obj){
       responseToJson(obj,getResponse());
    }

}
