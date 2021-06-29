package com.cdel.format.controller;

import com.cdel.format.utils.ParseToFormatHtml;
import com.cdel.format.utils.ResponseUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class IndexController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/downloadHtml", method = RequestMethod.POST)
    public void downLoadHtml(String content, String chapter, HttpServletResponse response) throws IOException {
        String parseContent = ParseToFormatHtml.parseStringToHtml(content, chapter);
        ResponseUtils.downloadFile(response, chapter, new ByteArrayInputStream(parseContent.getBytes(StandardCharsets.UTF_8)));
    }
}
