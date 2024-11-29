package websocket;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * 服务启动类
 * author: pengshuaifeng
 * 2023/10/27
 */
@EnableWebSocket
@SpringBootApplication
@ServletComponentScan(basePackages = {"websocket.filter"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
