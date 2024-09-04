package commons.resolver.web;

import commons.basics.MultipartResolver;
import commons.basics.ServletHolder;
import commons.model.annotations.excel.ExcelMark;
import commons.resolver.excel.ExcelMarkResolver;
import commons.utils.CollectionUtils;
import commons.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * excel handler方法参数解析器
 *
 * @author fulin-peng
 * 2024-09-03  15:21
 */
public class ExcelHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        String paramName = ServletHolder.getRequestHeader("x-import-excel-param_name");
        return parameter.hasParameterAnnotation(ExcelMark.class) && StringUtils.isNotEmpty(paramName);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        ExcelMark excelMark = parameter.getParameterAnnotation(ExcelMark.class);
        if (servletRequest != null) {
            //从请求里获取file
            List<MultipartFile> multipartFiles =
                    MultipartResolver.getMultipartFiles(ServletHolder.getRequestHeader("x-import-excel-param_name"), servletRequest);
            if (CollectionUtils.isNotEmpty(multipartFiles)) {
                if(multipartFiles.size()>1)
                    throw new RuntimeException("不支持多excel文件导入");
                ExcelMarkResolver executor = WebApplicationContextUtils
                        .getWebApplicationContext(ServletHolder.getServletContext()).getBean(excelMark.importClass());
                return executor.importData((multipartFiles.get(0)).getInputStream(), parameter.getMethod().getParameters()[parameter.getParameterIndex()]);
//                return executor.importData((multipartFiles.get(0)).getInputStream(), parameter.getParameter());  //低版本不支持
            }
        }
        return null;
    }
}
