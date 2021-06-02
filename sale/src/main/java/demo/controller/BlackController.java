package demo.controller;

import demo.base.utils.ResponseUtils;
import demo.bean.User;
import demo.bean.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
public class BlackController {
    Logger logger = LoggerFactory.getLogger(BlackController.class);
    @Value("${server.port}")
    String port;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/black", method = {RequestMethod.GET})
    public List<String> getBlackList(String version) {
        String[] black = {"张三", "cat", "李四2", port};
        return Arrays.asList(black);
    }

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void getBlackList(String version, HttpServletResponse response) {
        String[] black = {"张三", "cat", "李四1", port};
        ResponseUtils.download(null, "下载", response);
    }

    @RequestMapping(value = "/user/registry", method = RequestMethod.POST)
    public User createUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userService.create(username, password);
    }

    @RequestMapping("/hi")
    public String home() {
        return "hi+" + ",i am from port:" + port;
    }

    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String hello() {
        return "hello" + port;
    }

    @RequestMapping("/getPrinciple")
    public OAuth2Authentication getPrinciple(OAuth2Authentication oAuth2, Principal principal, Authentication authentication) {
        logger.info(oAuth2.getUserAuthentication().getAuthorities().toString());
        logger.info(oAuth2.toString());
        logger.info("principal.to string{}", principal.toString());
        logger.info("principla.getName = {}", principal.getName());
        logger.info("authentication:{}", authentication.getAuthorities().toString());
        return oAuth2;
    }
}
