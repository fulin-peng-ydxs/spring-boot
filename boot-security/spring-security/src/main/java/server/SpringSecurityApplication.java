package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author PengFuLin
 * @description 启动程序
 * @date 2022/8/7 19:30
 */
@SpringBootApplication
//开启@secured注解
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class,args);
    }
}
