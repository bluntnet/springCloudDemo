package demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminIndexController {

    @RequestMapping("/admin/index")
    public String adminIndex(Model model) {
        return "admin/index";
    }
}
