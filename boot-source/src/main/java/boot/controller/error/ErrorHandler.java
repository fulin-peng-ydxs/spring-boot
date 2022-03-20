package boot.controller.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: controller异常处理器
 * @date 2022/3/19 21:02
 */
@ControllerAdvice
public class ErrorHandler {

//    @ResponseBody
//    @ExceptionHandler(Exception.class)
//    public Map<String, Object> testError(Exception exception){
//        System.out.println(exception.getMessage());
//        Map<String , Object> objectObjectMap = new HashMap<>();
//        objectObjectMap.put("status", 500);
//        objectObjectMap.put("msg","异常处理");
//        return objectObjectMap;
//    }


    @ExceptionHandler(Exception.class)
    public String handleException(Exception e,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","system is error");
        map.put("message",e.getMessage());
        //转发到 ”/error“
        request.setAttribute("customer",map);
        return "forward:/error";
    }

}
