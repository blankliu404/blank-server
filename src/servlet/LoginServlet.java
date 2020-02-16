package servlet;

import server.inter.BasicServlet;
import server.service.Request;
import server.service.Response;

import java.io.IOException;

/**
 * LoginServlet
 * 测试使用服务器参数获取功能
 * @author Blank
 */
public class LoginServlet implements BasicServlet {
    @Override
    public void service(Request request, Response response) throws IOException {
        switch (request.getMethod()) {
            case "GET":
                request.forward("view/login.html");
                break;
            case "POST":
                // 登录业务
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                if (username.equals("blank") && password.equals("blank")) {
                    // 登录成功
                    request.forward("view/success.html");
                }else {
                    // 登录失败
                    request.forward("view/login.html");
                }
                break;
        }
    }
}
