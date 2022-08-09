package server.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PengFuLin
 * @description
 * @date 2022/8/9 20:50
 */
@RestController
public class AuthorityController {

    @GetMapping("hasAuthority")
    public String hasAuthority(){
        return "hasAuthority";
    }


    @GetMapping("hasAnyAuthority")
    public String hasAnyAuthority(){
        return "hasAnyAuthority";
    }


    @Secured("ROLE_admin")
    @GetMapping("secured")
    public String secured(){
        return "secured";
    }
}
