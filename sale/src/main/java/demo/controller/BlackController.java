package demo.controller;

import demo.base.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
public class BlackController {
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "/black", method = {RequestMethod.GET})
    public List<String> getBlackList(String version) {
        String[] black = {"张三", "cat", "李四2", port};
        return Arrays.asList(black);
    }

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void getBlackList(String version, HttpServletResponse response) {
        String[] black = {"张三", "cat", "李四1", port};
        ResponseUtils.download(null, "下载", response);
    }
}
