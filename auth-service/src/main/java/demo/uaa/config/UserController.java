package demo.uaa.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
    @RequestMapping(value = "/users/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }
}
