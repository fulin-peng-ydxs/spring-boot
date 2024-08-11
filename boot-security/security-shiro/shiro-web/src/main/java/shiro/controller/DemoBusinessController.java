package shiro.controller;


import commons.model.web.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demoÒµÎñ¿ØÖÆÆ÷
 *
 * @author pengshuaifeng
 * 2024/8/5
 */
@Slf4j
@RestController
@RequestMapping("/demo/business")
public class DemoBusinessController {

    @GetMapping("basic")
    public Response basic(){
        return Response.success();
    }

}
