package demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyUserService myUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
                .loginProcessingUrl("/login/authentication")
                .successHandler(myAuthenticationSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole(RoleConstant.ADMIN)
                .antMatchers("/user/**").hasRole(RoleConstant.USER)
                .antMatchers(getPermitAllList()).permitAll()
                .anyRequest()
                .authenticated();

        // //解决中文乱码问题
        // CharacterEncodingFilter filter = new CharacterEncodingFilter();
        // filter.setEncoding("UTF-8");
        // filter.setForceEncoding(true);
        // //
        // http.addFilterBefore(filter, CsrfFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserService).passwordEncoder(passwordEncoder());
    }

    private String[] getPermitAllList() {
        return new String[]{"/actuator/**","/static/**", "/login/**","/swagger-ui.html","/webjars/**","/hystrix/**","/hystrix.stream/**"};
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String userPassword = encoder.encode("user");
        System.out.println(userPassword);
    }
}
