package web.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@EnableRedisWebSession
@SpringBootApplication
public class WebfluxBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxBootApplication.class, args);
    }

}
