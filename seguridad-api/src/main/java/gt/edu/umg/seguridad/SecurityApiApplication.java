package gt.edu.umg.seguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "gt.edu.umg")
@EnableJpaRepositories(basePackages = "gt.edu.umg.dao")
public class SecurityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApiApplication.class, args);
        System.out.println("Seguridad-API iniciada correctamente!");
        System.out.println("Swagger: http://localhost:8081/swagger-ui/index.html");
        System.out.println("Endpoint de login: POST http://localhost:8081/auth/login");
    }
}
