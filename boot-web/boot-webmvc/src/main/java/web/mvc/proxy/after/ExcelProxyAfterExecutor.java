package web.mvc.proxy.after;

import commons.basics.ServletHolder;
import commons.model.annotations.excel.ExcelExport;
import commons.model.web.mime.MimeType;
import commons.service.excel.ExcelExportExecutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import java.lang.reflect.Method;

/**
 * excel代理后置处理器
 *
 * @author pengshuaifeng
 * 2024/1/21
 */
@Component
public class ExcelProxyAfterExecutor extends ProxyAfterExecutor {

    @Override
    protected Object execute(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String accept = ServletHolder.getRequestHeader("x-download-excel");
            if(accept==null || result==null)
                return result;
            if (accept.equals(MimeType.APPLICATION_XLSX) || accept.equals(MimeType.APPLICATION_XLS)) {  //请求接受excel响应
                ExcelExport excelExport = method.getAnnotation(ExcelExport.class);
                if(excelExport !=null){   //定义了excel导出注解
                    //导出数据
                    boolean isXlsx = !accept.equals(MimeType.APPLICATION_XLS);
                    ExcelExportExecutor executor = WebApplicationContextUtils.getWebApplicationContext(ServletHolder.getServletContext()).getBean(
                            excelExport.executorClass());
                    byte[] bytes =executor.exportByte(method,result,isXlsx);
                    ServletHolder.responseToOutStream(null,bytes, excelExport.fileName(),
                            isXlsx?MimeType.APPLICATION_XLSX:MimeType.APPLICATION_XLS,false);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("excel数据导出异常",e);
        }
    }

}
