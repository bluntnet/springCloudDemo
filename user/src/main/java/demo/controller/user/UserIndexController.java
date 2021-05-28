package demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserIndexController {

    @RequestMapping(value = "/user/index", method = RequestMethod.GET)
    public String userIndex(Model model) {
        return "user/index";
    }

}
