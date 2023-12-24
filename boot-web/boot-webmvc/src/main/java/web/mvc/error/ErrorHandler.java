package web.mvc.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import web.mvc.model.web.response.Response;
import web.mvc.model.web.response.ResponseStatus;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * controller异常处理器
 * 2023/12/20 00:08
 * @author pengshuaifeng
 */
@Slf4j
@ControllerAdvice
public class ErrorHandler {

    /**
     * 通用异常返回
     * 2023/12/20 22:44
     * @author pengshuaifeng
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e){
        log.error("系统异常",e);
        return Response.failure();
    }

    /**
     * 校验异常返回
     * 2023/12/24 13:43
     * @author pengshuaifeng
     */
    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public Response validationHandleException(Exception e){
        log.error("系统校验异常",e);
        return Response.custom(ResponseStatus.PARAMS_CHECK_FAILURE,e.getMessage());
    }

    /**
     * 默认异常请求转发处理
     * 2023/12/20 22:45
     * @author pengshuaifeng
     */
    public String forwardHandleException(Exception e,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","system is error");
        map.put("message",e.getMessage());
        //转发到 ”/error“
        request.setAttribute("customer",map);
        return "forward:/error";
    }
}
