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
        if (request.getMethod().equals("GET")) request.forward("view/index.html");
        String[] likes = request.getParameterValues("like");
        if (likes != null) {
            for (String like : likes) {
                System.out.println(like);
            }
        }
    }
}
