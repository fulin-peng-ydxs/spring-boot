package boot.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 错误处理测试控制器
 * @date 2022/3/19 16:12
 */

@Controller
@RequestMapping("error")
public class ErrorController {


    @GetMapping("/getErrorResponse")
    public ModelAndView getErrorResponse(Map map) throws Exception {
        throw new Exception("错误抛出");
    }
}
