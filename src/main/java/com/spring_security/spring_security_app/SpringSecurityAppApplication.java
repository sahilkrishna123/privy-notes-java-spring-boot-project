package com.spring_security.spring_security_app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Privy Notes",
				description = "Private Notes management application with Spring Security",
				version = "1.0.0",
				contact = @Contact(
						name = "Sahil Krishna",
						email = "sahilkrishna1243@gmail.com",
						url = "https://github.com/sahilkrishna123"
				),
				license = @License(
						name = "Apache 2.0"
				)
		)
)
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

}
