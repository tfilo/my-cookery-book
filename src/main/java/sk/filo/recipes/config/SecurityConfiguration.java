package sk.filo.recipes.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author tomas
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers(
                            "/css/*.css", 
                            "/css/fontawesome/css/*.css", 
                            "/css/fontawesome/webfonts/fa-solid-900.*", 
                            "/js/*.js", 
                            "/login.html", 
                            "/login-error.html"
                    ).permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/recipe/**").hasRole("USER")
                    .antMatchers("/user/**").hasRole("USER")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login.html")
                    .failureUrl("/login-error.html")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login.html");
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
        return new BCryptPasswordEncoder();
    }

}
