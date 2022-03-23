package boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 启动类
 * @date 2022/3/7 16:43
 */
@SpringBootApplication
public class ApplicationStart {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class,args);
    }
}
