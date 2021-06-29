package com.cdel.format.utils;


import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResponseUtils {
    public static void downloadFile(HttpServletResponse resp, String filename, InputStream inputStream) throws IOException {
        String fileName = filename.substring(filename.lastIndexOf("\\") + 1);
        resp.setContentType("mutipart/form-data");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream outputStream = resp.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }
}
