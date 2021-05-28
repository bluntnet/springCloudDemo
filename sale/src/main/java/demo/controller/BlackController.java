package demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BlackController {
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "/black", method = {RequestMethod.GET})
    public List<String> getBlackList(String version) {
        String[] black = {"张三", "cat", "李四1", port};
        return Arrays.asList(black);
    }
}
