package server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author PengFuLin
 * @description
 * @date 2022/8/7 21:05
 */
@RestController
public class LoginController {

    @PostMapping ("success")
    public String encoding(){
        System.out.println("你好");
        return "你好";
    }
    @PostMapping ("failure")
    public String failure(){
        System.out.println("失败");
        return "失败";
    }



}
