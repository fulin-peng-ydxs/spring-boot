package shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import shiro.configure.ShiroWebConfigure;

/**
 * @author PengFuLin
 * @description shiro∆Ù∂Ø≥Ã–Ú
 * @date 2022/8/13 12:36
 */
@SpringBootApplication
public class ShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(
                ShiroApplication.class, args);
    }
}
