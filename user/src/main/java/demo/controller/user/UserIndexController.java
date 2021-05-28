package demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserIndexController {

    @RequestMapping("/user/index")
    public String userIndex(Model model) {
        return "user/index";
    }

}
