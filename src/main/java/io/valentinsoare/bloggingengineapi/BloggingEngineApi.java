package io.valentinsoare.bloggingengineapi;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Blogging Engine API",
				version = "0.1",
				description = "One stop shop for a blogging API.",
				contact = @Contact(
						name = "Valentin Soare",
						email = "soarevalentinn@gmail.com",
						url = "https://www.linkedin.com/in/valentin-soare"
				),
				license = @License(
						name = "Apache 2.0",
						url = "http://www.apache.org/licenses/LICENSE-2.0.html"
				),
				termsOfService = "http://swagger.io/terms/"
		),
		externalDocs = @ExternalDocumentation(
				description = "Blogging Engine API documentation",
				url = "https://github.com/valentinnsoare/bloggingengineapi"
		)
)
@SpringBootApplication
public class BloggingEngineApi {
	public static void main(String[] args) {
		SpringApplication.run(BloggingEngineApi.class, args);
	}
}
