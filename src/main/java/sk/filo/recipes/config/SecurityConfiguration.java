package sk.filo.recipes.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author tomas
 */
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/recipe/**").hasRole("USER")
                .antMatchers("/recipe/view/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                          "SELECT username, password, enabled "
                        + "FROM cb_user "
                        + "WHERE username = ?"
                )
                .authoritiesByUsernameQuery(
                          "SELECT username, name "
                        + "from cb_user_role "
                        + "join cb_user ON cb_user.id = cb_user_role.user_id "
                        + "join cb_role ON cb_role.id = cb_user_role.role_id "
                        + "where cb_user.username = ?"
                );
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256");
        return new BCryptPasswordEncoder();
    }

}
