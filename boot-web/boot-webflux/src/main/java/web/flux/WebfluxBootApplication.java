package web.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@EnableRedisWebSession(maxInactiveIntervalInSeconds = 3600)
@SpringBootApplication
public class WebfluxBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxBootApplication.class, args);
    }

}
