package demo.config;


import demo.service.MyUserService;
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
                .usernameParameter("username").passwordParameter("password")// 字段名字
                .loginProcessingUrl("/login/authentication") // 点击提交访问的 url
                .successHandler(myAuthenticationSuccessHandler) // authentication 认证成功之后执行的方法
                .and()
                .authorizeRequests() //对 url 增加验证授权
                .antMatchers("/admin/**").hasRole(RoleConstant.ADMIN)// 有 admin 授权的人可以访问
                .antMatchers("/user/**").hasRole(RoleConstant.USER)// 有 user 授权的人可以访问
                .antMatchers(getPermitAllList()).permitAll()//都可以访问
                .anyRequest()//别的 url 跳转到登录页面
                .authenticated();


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
