package sk.filo.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sk.filo.recipes.config.ConfigProperties;

@SpringBootApplication(scanBasePackages = "sk.filo.recipes")
@EnableConfigurationProperties(ConfigProperties.class)
public class RecipesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipesApplication.class, args);
	}

}
