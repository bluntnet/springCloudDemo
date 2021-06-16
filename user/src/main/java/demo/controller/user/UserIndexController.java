package demo.controller.user;

import demo.bean.Account;
import demo.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserIndexController extends BaseController {

    @RequestMapping(value = "/user/index", method = RequestMethod.GET)
    public String userIndex(Model model) {

        return "user/index";
    }

    @ResponseBody
    @RequestMapping(value = "/user/get", method = RequestMethod.GET)
    public Account getAccount() {
        return getCurrentAccount();
    }


}
