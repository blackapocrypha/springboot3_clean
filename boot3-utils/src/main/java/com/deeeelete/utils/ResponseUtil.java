package com.deeeelete.utils;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {

    public static void out(HttpServletResponse response, JsonResult result) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            mapper.writeValue(response.getWriter(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void buildJsonReturn(HttpServletResponse response, JsonResult result) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }
}