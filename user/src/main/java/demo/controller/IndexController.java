package demo.controller;

import demo.bean.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model){
        Account user = new Account();
        model.addAttribute("user", user);
        return "index";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
        Account user = new Account();
        model.addAttribute("user", user);
        return "login";
    }
}
