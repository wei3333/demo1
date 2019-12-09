package com.example.demo1.filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Order(1)
@WebFilter(filterName = "Filter0_PermissionFilter", urlPatterns = "/*")
public class PermissionFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("User Permission Checking...");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getRequestURI();
        System.out.println(path);
        if(!path.contains("/login") && !path.contains("/myapp/")){
            JSONObject message = new JSONObject();
            HttpSession session = request.getSession(false);
            if(session == null || session.getAttribute("currentUser") == null){
                message.put("message","请先登录/重新登录");
                response.getWriter().println(message);
                return;
            }
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
