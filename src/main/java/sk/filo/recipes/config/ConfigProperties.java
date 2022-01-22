package sk.filo.recipes.config;

import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.validation.constraints.NotEmpty;

/**
 *
 * @author tomas
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("app")
@Validated
public class ConfigProperties implements WebMvcConfigurer {

    @NotEmpty
    private String baseUrl;

    @NotEmpty
    private String rememberMeKey;

    @NotEmpty
    private String locale;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames("classpath:/messages/messages");
        return messageSource;
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale(locale));
        return slr;
    }

}

