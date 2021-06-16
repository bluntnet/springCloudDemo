package demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Configuration
public class AuthenticationHandler {
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                log.debug("===onAuthenticationSuccess==={}", authentication);

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                boolean isAdmin = false;
                if (userDetails.getAuthorities() != null && userDetails.getAuthorities().size() > 0) {
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                    for (GrantedAuthority grant : authorities) {
                        log.debug("list user grant " + grant);
                        if (grant.getAuthority().contains(RoleConstant.ADMIN)) {
                            isAdmin = true;
                        }
                    }
                }
                StringBuilder sb = new StringBuilder(request.getContextPath());
                if (isAdmin) {
                    sb.append("/admin/index");
                } else {
                    sb.append("/user/index");
                }
                response.sendRedirect(sb.toString());
            }
        };
        return authenticationSuccessHandler;
    }
}
