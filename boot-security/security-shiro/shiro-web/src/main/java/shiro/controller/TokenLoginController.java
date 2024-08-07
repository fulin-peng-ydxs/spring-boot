package shiro.controller;


import commons.model.web.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * token用户登录
 *
 * @author pengshuaifeng
 * 2024/8/4
 */
@Slf4j
@Controller
@RequestMapping("/token/login")
public class TokenLoginController {

    /**
     * 用户登录
     */
    @RequestMapping("auth")
    public Response login(String username, String password) {
        try {
            //获取主体对象
            Subject subject = SecurityUtils.getSubject();
            subject.login(new UsernamePasswordToken(username, password));
            Object principal = SecurityUtils.getSubject().getPrincipal();
            return Response.success(principal);
        } catch (AuthenticationException e) {
            log.error("登录认证异常",e);
            return Response.failure("登录失败");
        }
    }

}
