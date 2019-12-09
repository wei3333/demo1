package com.example.demo1.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class LogoutController {
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    protected void out(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        JSONObject message = new JSONObject();
        if (session != null){
            session.invalidate();
            message.put("message","已退出");
            response.getWriter().println(message);
            return;
        }
    }
}