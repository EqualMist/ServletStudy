package com.zzy.webtest;

import com.zzy.webtest.enity.User;
import com.zzy.webtest.mapper.UserMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet(name = "TimeServlet", urlPatterns = {"/time"}, loadOnStartup = 3)
public class TimeServlet extends HttpServlet {

    SqlSessionFactory factory;

    @SneakyThrows
    @Override
    public void init() throws ServletException {
        factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型
        resp.setContentType("text/html;charset=UTF-8");

        // 测试获取Cookie
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                System.out.println("Cookie Name: " + cookie.getName() + " Cookie Value: " + cookie.getValue());
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(System.currentTimeMillis());
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("当前时间：" + currentTime);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(System.currentTimeMillis());
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("当前时间：" + currentTime);
        resp.getWriter().println("当前登录用户：" + ((User)req.getAttribute("user")).getUsername());
    }
}
