package gt.edu.umg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "gt.edu.umg",
    "gt.edu.umg.seguridad",
    "gt.edu.umg.security",
    "gt.edu.umg.config",
    "gt.edu.umg.controller",
    "gt.edu.umg.service",
    "gt.edu.umg.dao"
})
@EnableJpaRepositories(basePackages = "gt.edu.umg.dao")
public class SecurityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApiApplication.class, args);
        System.out.println("Seguridad-API iniciada correctamente!");
        System.out.println("Swagger: http://localhost:8081/swagger-ui/index.html");
        System.out.println("Endpoint de login: POST http://localhost:8081/auth/login");
    }
}