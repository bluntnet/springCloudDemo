package com.cdel.format.controller;

import com.cdel.format.bean.InputBean;
import com.cdel.format.utils.ParseToFormatHtml;
import com.cdel.format.utils.SimpleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class IndexController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult convert(InputBean inputBean) {
        ParseToFormatHtml parse = new ParseToFormatHtml(inputBean);
        String parseContent = parse.parseStringToHtml();
        String content = parse.handleImageTag(parseContent);
        return SimpleResult.success(content);
    }
}
