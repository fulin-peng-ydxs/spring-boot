package web.flux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Demo控制层
 *
 * @author PengFuLin
 * 2022/10/3 21:00
 */
@Controller
@RequestMapping("demo")
public class DemoController {

    @ResponseBody
    @GetMapping("/getOne")
    public String getOne(){
        return "hello";
    }
}