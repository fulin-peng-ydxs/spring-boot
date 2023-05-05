package jpa;

import jpa.entities.User;
import jpa.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@EntityScan("jpa.entities")  // µÃÂ…®√Ë
@SpringBootApplication
public class ApplicationJpa {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ApplicationJpa.class, args);
        UserRepository bean = run.getBean(UserRepository.class);
        User userById = bean.getUserById(1);
        System.out.println(userById);
    }
}
