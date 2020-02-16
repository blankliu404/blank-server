package server.inter;

import server.service.Request;
import server.service.Response;

import java.io.IOException;

/**
 * 基础Servlet接口
 * @author Blank
 */
public interface BasicServlet {
    /**
     * 服务方法，处理请求
     * @param request
     * @param response
     */
    void service(Request request, Response response) throws IOException;
}
