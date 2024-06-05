package com.zzy.webtest.servlet;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@MultipartConfig
@WebServlet(name = "FileServlet", urlPatterns = {"/fileUpload"}, loadOnStartup = 2)
public class FileServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * 文件下载
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");
        OutputStream outputStream = resp.getOutputStream();
        InputStream inputStream = Resources.getResourceAsStream("Yae Miko.jpg");
        // 直接使用copy方法完成转换
        IOUtils.copy(inputStream, outputStream);
        System.out.println("123");
    }

    /**
     * 文件上传
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (FileOutputStream stream = new FileOutputStream("D:\\workspace\\Java\\web_test\\upload.jpg")) {
            Part part = req.getPart("test-file");
            IOUtils.copy(part.getInputStream(), stream);
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("文件上传成功！");
        }
    }
}
