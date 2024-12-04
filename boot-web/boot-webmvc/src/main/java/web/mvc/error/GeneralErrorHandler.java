package web.mvc.error;

import commons.model.exception.GeneralBusinessException;
import commons.model.web.response.Response;
import commons.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;



/**
 * 通用controller异常处理器
 * 2023/12/20 00:08
 * @author pengshuaifeng
 */
@Slf4j
@ControllerAdvice
public class GeneralErrorHandler {

    /**
     * 默认异常处理：json
     * 2023/12/20 22:44
     * @author pengshuaifeng
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<?> defaultHandle(Exception e){
        log.error("系统异常：",e);
        return Response.failure();
    }


    /**
     * 系统根异常处理：json
     * 2023/12/20 22:44
     * @author pengshuaifeng
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<?> generalSystemRootHandle(Exception e){
        log.error("系统root异常：",e);
        return Response.failure(ExceptionUtils.getRootCause(e).getMessage());
    }

    /**
     * 业务异常处理：json
     * 2024/1/21 16:25
     * @author pengshuaifeng
     */
    @ResponseBody
    @ExceptionHandler(GeneralBusinessException.class)
    public Response<?> generalBusinessHandle(GeneralBusinessException e){
        log.error("系统业务异常：",e);
        return Response.custom(e.getExceptionType(),e.getExceptionBody());
    }

    /**
     * 异常处理：转发
     * 2023/12/20 22:45
     * @author pengshuaifeng
     */
    public String forwardHandle(Exception e,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","system is error");
        map.put("message",e.getMessage());
        //转发到 ”/error“
        request.setAttribute("customer",map);
        return "forward:/error";
    }
}
