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
import java.util.Map;

@WebServlet(name = "LoginServlet", loadOnStartup = 1, urlPatterns = {"/login"} )
public class LoginServlet extends HttpServlet {

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
        if (req.getCookies() != null) {
            String username = "";
            String password = "";
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }
            try (SqlSession sqlSession = factory.openSession(true)){
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
                User user = userMapper.getUser(username, password);
                if (user != null) {
                    resp.getWriter().write("记住我操作成功，当前登录用户：" + user.getUsername());
                    resp.sendRedirect("time");
                    return;
                } else {
                    Cookie useernameCookie = new Cookie("username", username);
                    useernameCookie.setMaxAge(0);
                    Cookie passwordCookie = new Cookie("password", password);
                    passwordCookie.setMaxAge(0);
                    resp.addCookie(useernameCookie);
                    resp.addCookie(passwordCookie);// 0s

                }
            }
        }
//        req.getRequestDispatcher("/").forward(req, resp);   //正常情况还是转发给默认的Servlet帮我们返回静态页面
        resp.sendRedirect("index.html");   //转发到index.html
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型
        resp.setContentType("text/html;charset=UTF-8");
        // 获取POST请求携带的表单数据
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 判断表单是否完整
        if (parameterMap.containsKey("username") && parameterMap.containsKey("password")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            try (SqlSession session = factory.openSession(true)) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                // 权限校验
                User loginUser = userMapper.getUser(username, password);
                if (loginUser != null) {

                    // 重定向
//                    resp.sendRedirect("time");


                    // Cookie
//                    Cookie cookie = new Cookie("tempCookie", "tempCookieValue");
//                    resp.addCookie(cookie);
//                    resp.sendRedirect( "time");

                    // 记住我，后端实现
                    if (parameterMap.containsKey("remember-me")) {
                        Cookie useernameCookie = new Cookie("username", username);
                        useernameCookie.setMaxAge(30); // 30s
                        Cookie passwordCookie = new Cookie("password", password);
                        passwordCookie.setMaxAge(30); // 30s
                        resp.addCookie(useernameCookie);
                        resp.addCookie(passwordCookie);

                    }
                    // 请求转发
                    req.setAttribute("user", loginUser);
                    req.getRequestDispatcher("time").forward(req, resp);

//                    resp.getWriter().write("用户登陆成功，登录的用户为：" + loginUser.toString());
                }else {
                    resp.getWriter().println("用户不存在或者用户名密码有误");
                }
            }
        } else {
            resp.getWriter().println("表单数据不完整");
        }

    }
}
