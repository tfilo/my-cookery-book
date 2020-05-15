package sk.filo.recipes.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString
@ConfigurationProperties
@Validated
public class ConfigProperties implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames("classpath:/messages/messages");
        return messageSource;
    }
    
}

