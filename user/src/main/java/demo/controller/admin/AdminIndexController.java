package demo.controller.admin;

import com.netflix.discovery.converters.Auto;
import demo.bean.Account;
import demo.controller.BaseController;
import demo.helper.RedisDao;
import demo.helper.Result;
import demo.service.RibbonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(tags = "管理接口", value = "测试类")
public class AdminIndexController extends BaseController {

    @Autowired
    RedisDao redisDao;
    @Autowired
    RibbonService ribbonService;

    @RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    public String adminIndex(Model model) {
        Account currentAccount = getCurrentAccount();
        redisDao.setKeyValue("admin", currentAccount);
        return "admin/index";
    }

    @RequestMapping(value = "/admin/fromRedis", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "从 Redis 中取得用户", notes = "从 Redis 取得用户 from notes")
    public Result realName() {
        Account admin = (Account) redisDao.getKeyValue("admin");
        return Result.ok(admin);
    }

    @ApiOperation(value = "增加一个用户", notes = "从 Redis 取得用户 from notes")
    @ResponseBody
    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    public Result add(@RequestBody Account account) {

        return Result.ok(account);
    }

    @ResponseBody
    @RequestMapping(value="/admin/black",method = RequestMethod.GET)
    public List<String> getBlack(){
        return ribbonService.getUser();
    }
}
