package demo.controller;

import demo.bean.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/index")
    public String index(Model model){
        Account user = new Account();
        model.addAttribute("user", user);
        return "index";
    }
    @RequestMapping("/login")
    public String login(Model model){
        Account user = new Account();
        model.addAttribute("user", user);
        return "login";
    }
}
