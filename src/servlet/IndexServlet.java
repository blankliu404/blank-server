package servlet;

import server.inter.BasicServlet;
import server.service.Request;
import server.service.Response;

import java.io.IOException;

/**
 * IndexServlet
 * 测试使用服务器转发到页面功能
 * @author Blank
 */
public class IndexServlet implements BasicServlet {
    @Override
    public void service(Request request, Response response) throws IOException {
        request.forward("view/index.html");
    }
}
