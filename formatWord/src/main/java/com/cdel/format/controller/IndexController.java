package com.cdel.format.controller;

import com.cdel.format.utils.ParseToFormatHtml;
import com.cdel.format.utils.SimpleResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/downloadHtml", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult downLoadHtml(String content, String chapter) {
        String parseContent = ParseToFormatHtml.parseStringToHtml(content, chapter);
        return SimpleResult.success(parseContent);
    }
}
