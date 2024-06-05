package com.zzy.webtest.filter;

import com.zzy.webtest.enity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class MainFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String url = req.getRequestURL().toString();
        // 判断是否为静态资源
        if (!url.endsWith(".html") &&!url.endsWith(".css") &&!url.endsWith(".js")) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            // 判断用户是否登录
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "login");
                return;
            }
        }
        // 交给过滤链处理
        chain.doFilter(req, res);
    }
}
