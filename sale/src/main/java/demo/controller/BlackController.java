package demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BlackController {

    @RequestMapping(value = "/black",method = {RequestMethod.GET})
    public List<String> getBlackList(String version) {
        String[] black = {"张三", "cat", "李四"};
        return Arrays.asList(black);
    }
}
