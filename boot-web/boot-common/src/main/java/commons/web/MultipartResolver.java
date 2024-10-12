package commons.web;

import commons.utils.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 多部分解析器
 * 2024-09-04  10:21
 * @see MultipartResolutionDelegate
 * @author fulin-peng
 */
public class MultipartResolver {

    /**
     * 获取文件
     * 2024/9/3 下午6:47
     * @param paramName 文件请求参数名
     * @param request 请求对象
     * @author fulin-peng
     * @return MultiPartFile数组对象
     */
    public static List<MultipartFile> getMultipartFiles(String paramName, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
        boolean isMultipart = (multipartRequest != null || isMultipartContent(request));
        if (!isMultipart)
            return null;
        if (multipartRequest == null) {
            multipartRequest = new StandardMultipartHttpServletRequest(request);
        }
        return multipartRequest.getFiles(paramName);
    }

    public static Object getMultipartFile(String paramName,HttpServletRequest request) {
        List<MultipartFile> multipartFiles = getMultipartFiles(paramName, request);
        if (CollectionUtils.isEmpty(multipartFiles))
            return null;
        return multipartFiles.get(0);
    }


    private static boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
    }
}
