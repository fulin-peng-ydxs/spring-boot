package commons.holder;


import commons.model.web.mime.MimeType;
import commons.utils.JsonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * "servlet工具类
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
     * POST /some/path.html HTTP/1.1
     * /some/path.html
     * GET http://foo.bar/a.html HTTP/1.0
     * /a.html
     * HEAD /xyz?a=b HTTP/1.1
     * /xyz
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
        return getRequestHeader(headName,getRequest());
    }

    public static String getRequestHeader(String headName,HttpServletRequest request) {
        return request.getHeader(headName);
    }

    /**
     * 获取请求参数
     * 2024/6/26 0026 10:10
     * @author fulin-peng
     * @return 返回map集合
     */
    public static Map<String, String[]> getRequestParamMaps(HttpServletRequest request) {
        return request.getParameterMap();
    }

    /**
     * 获取请求参数
     * 2024/6/26 0026 10:10
     * @author fulin-peng
     * @return 返回单个参数数组
     */
    public static String[] getRequestParams(String paramName,HttpServletRequest request) {
        return getRequestParamMaps(request).get(paramName);
    }

    /**
     * 获取请求参数
     * 2024/6/26 0026 10:10
     * @author fulin-peng
     * @return 返回单个参数第一个值
     */
    public static String getRequestParam(String paramName,HttpServletRequest request) {
        return request.getParameter(paramName);
    }

    /**
     * 获取请求cookie
     * 2024/6/26 0026 10:15
     * @author fulin-peng
     */
    public static Cookie[] getCookies(HttpServletRequest request){
        return request.getCookies();
    }

    /**
     * 获取请求cookie键值对
     * 2024/6/26 0026 10:15
     * @author fulin-peng
     */
    public static Map<String, String> getCookieMap(HttpServletRequest request){
        return Arrays.stream(getCookies(request)).collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }

    /**
     * 获取请求cookie值
     * 2024/6/26 0026 10:15
     * @author fulin-peng
     */
    public static String getCookieValue(String cookieName,HttpServletRequest request){
        return getCookieMap(request).get(cookieName);
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
     * 请求响应：文件流头设置
     * 2023/12/30 13:18
     * @param contentLength 文件字节大小
     * @param fileName 文件响应名
     * @param mimeType 文件响应类型
     * @param preview 是否直接预览
     * @author pengshuaifeng
     */
    public static void responseToOutStreamForSetHeader(HttpServletResponse response,Integer contentLength,String fileName,String mimeType,boolean preview){
        try {
            //1.设置内容信息描述
            //1.1文件名进行Url参数格式编码
            String encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, preview?"inline":"attachment; filename*=UTF-8''" + encodedFilename);
            //2.设置内容的类型&大小
            response.setContentType(mimeType==null? MimeType.APPLICATION_OCTET_STREAM :mimeType);
            response.setHeader(HttpHeaders.CONTENT_LENGTH, Integer.toString(contentLength));
        } catch (Exception e) {
            throw new RuntimeException("请求文件流响应异常",e);
        }
    }

    public static void responseToOutStreamForSetHeader(Integer contentLength,String fileName,String mimeType,boolean preview){
        responseToOutStreamForSetHeader(getResponse(),contentLength,fileName,mimeType,preview);
    }


    /**
     * 请求响应：文件流
     * 2023/12/30 13:18
     * @param response 请求响应对象
     * @param in 文件流
     * @param bufferSize 文件流读取缓存区大小
     * @param fileName 文件响应名
     * @param mimeType 文件响应类型
     * @param preview 是否直接预览
     * @author pengshuaifeng
     */
    public static void responseToOutStream(HttpServletResponse response, InputStream in,int bufferSize,String fileName, String mimeType,boolean preview){
        try (InputStream temp=in){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[bufferSize<=0?4096:bufferSize];
            int bytesRead;
            while ((bytesRead = temp.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            responseToOutStream(response,byteArrayOutputStream.toByteArray(),fileName,mimeType,preview);
        } catch (Exception e) {
            throw new RuntimeException("请求文件流响应异常",e);
        }
    }
    public static void responseToOutStream(InputStream in,int bufferSize,String fileName, String mimeType,boolean preview){
        responseToOutStream(null,in,bufferSize,fileName,mimeType,preview);
    }

    /**
     * 请求响应：文件流
     * 2024/5/15 0015 17:50
     * @author fulin-peng
     */
    public static void responseToOutStream(byte[] content,String fileName,String mimeType,boolean preview){
        responseToOutStream(null,content,fileName,mimeType,preview);
    }


    /**
     * 请求响应：文件流
     * 2023/12/30 13:18
     * @param response 请求响应对象
     * @param content 文件字节数据
     * @param fileName 文件响应名
     * @param mimeType 文件响应类型
     * @param preview 是否直接预览
     * @author pengshuaifeng
     */
    public static void responseToOutStream(HttpServletResponse response,byte[] content,String fileName,String mimeType,boolean preview){
        try {
            response=response==null?getResponse():response;
            responseToOutStreamForSetHeader(response,content.length,fileName,mimeType,preview);
            //3.写入内容到响应流&刷新
            response.getOutputStream().write(content);
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
        responseToJson(obj,response,HttpStatus.OK);
    }

    public static void responseToJson(Object obj, HttpServletResponse response, HttpStatus status){
        try {
            response.setStatus(status.value());
            String responseJson = JsonUtils.getString(obj);
            response.setContentType(MimeType.APPLICATION_JSON_UTF_8);
            response.getWriter().write(responseJson);
        } catch (Exception e) {
            throw new RuntimeException("请求Json响应异常",e);
        }
    }

    public static void responseToJson(Object obj){
       responseToJson(obj,getResponse());
    }

    public static void responseToJson(Object obj,HttpStatus status){
        responseToJson(obj,getResponse(),status);
    }

    /**
     * 请求重定向
     * 2024/8/5 21:31
     * @author pengshuaifeng
     */
    public static void redirect(String redirectUrl,HttpServletResponse response){
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader(HttpHeaders.LOCATION,redirectUrl);
    }

    public static void redirect(String redirectUrl){
        redirect(redirectUrl,getResponse());
    }


    /**
     * 是否为Ajax请求
     * 2024/8/5 20:25
     * @author pengshuaifeng
     */
    public static boolean isAjaxRequest(){
        String requestHeader = getRequestHeader("x-requested-with");
        return requestHeader != null && requestHeader.equals("XMLHttpRequest");
    }


    /**
     * 是否为json请求
     * 2024/8/5 22:16
     * @author pengshuaifeng
     */
    public static boolean isJsonRequest() {
        String contentType = getRequestHeader(HttpHeaders.ACCEPT);
        return isAjaxRequest() || contentType != null && (contentType.contains(MimeType.APPLICATION_JSON) || contentType.contains(MimeType.ALL));
    }

}
