package demo.controller.admin;

import demo.bean.Account;
import demo.controller.BaseController;
import demo.helper.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AdminIndexController extends BaseController {

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/admin/index")
    public String adminIndex(Model model) {
        Account currentAccount = getCurrentAccount();
        redisTemplate.opsForValue().set("admin",currentAccount);
        System.out.println("===========currentAccount = " + currentAccount);
        return "admin/index";
    }
    @RequestMapping("/admin/fromRedis")
    @ResponseBody
    public Result realName() {
        Account admin = (Account) redisTemplate.opsForValue().get("admin");
        return Result.ok(admin);
    }
    @RequestMapping("/admin/add")
    @ResponseBody
    public Result add() {

        Account admin = (Account) redisTemplate.opsForValue().get("admin");
        return Result.ok(admin);
    }
}
