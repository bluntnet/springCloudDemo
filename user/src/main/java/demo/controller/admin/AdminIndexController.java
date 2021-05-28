package demo.controller.admin;

import demo.bean.Account;
import demo.controller.BaseController;
import demo.helper.RedisDao;
import demo.helper.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "管理接口", value = "测试类")
public class AdminIndexController extends BaseController {

    @Autowired
    RedisDao redisDao;

    @RequestMapping("/admin/index")
    public String adminIndex(Model model) {
        Account currentAccount = getCurrentAccount();
        redisDao.setKeyValue("admin", currentAccount);
        System.out.println("===========currentAccount = " + currentAccount);
        return "admin/index";
    }

    @RequestMapping("/admin/fromRedis")
    @ResponseBody
    @ApiOperation(value = "从 Redis 中取得用户", notes = "从 Redis 取得用户 from notes")
    public Result realName() {
        Account admin = (Account) redisDao.getKeyValue("admin");
        return Result.ok(admin);
    }

    @ApiIgnore // 忽略这个api
    @RequestMapping("/admin/add")
    @ResponseBody
    public Result add() {
        Account admin = (Account) redisDao.getKeyValue("admin");
        return Result.ok(admin);
    }
}
