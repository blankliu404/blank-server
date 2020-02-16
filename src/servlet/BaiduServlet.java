package servlet;

import server.inter.BasicServlet;
import server.service.Request;
import server.service.Response;

import java.io.IOException;

/**
 * BaiduServlet
 * 测试服务器重定向到页面功能
 * @author Blank
 */
public class BaiduServlet implements BasicServlet {
    @Override
    public void service(Request request, Response response) throws IOException {
        response.redirect("http://www.baidu.com");
    }
}
