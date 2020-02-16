package server.service;

import server.xmlutil.ServerHandler;
import server.inter.BasicServlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * 请求分发器
 * @author Blank
 */
public class Dispatcher extends Thread {
    // 常量
    public static final String CRLF = "\r\n";
    public static final String SP = " ";
    // Socket
    private Socket accept;
    // 请求头的第一行信息
    private String requestHeader;
    // post请求的参数字符串
    private String postParamStr;

    public Dispatcher(Socket accept) {
        this.accept = accept;
    }

    @Override
    public void run() {
        try {
            init();
            dispatch();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转发
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void dispatch() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        Request request = new Request(accept,requestHeader,postParamStr);
        Response response = new Response();
        String route = request.getRoute();
        if (route.equals("") || route.equals("/")) {
            String welcomePage = ServerHandler.getHandler().getWelcomePage();
            if (welcomePage == null || welcomePage.equals(""))
                welcomePage = "server/defaultpage/index.html";
            request.forward(welcomePage);
        }else {
            String errorPage = ServerHandler.getHandler().getErrorPage();
            if (errorPage == null || errorPage.equals(""))
                errorPage = "server/defaultpage/error.html";
            Map<String, String> servletMappings = ServerHandler.getHandler().getServletMappings();
            if (servletMappings.containsKey(route)) {
                String name = servletMappings.get(route);
                Map<String, String> servlets = ServerHandler.getHandler().getServlets();
                if (servlets.containsKey(name)) {
                    // Servlet定义了
                    String className = servlets.get(name);
                    BasicServlet servlet = (BasicServlet) Class.forName(className).newInstance();
                    servlet.service(request, response);
                } else {
                    // Servlet未定义
                    request.forward(errorPage);
                }
            } else {
                // 路由不存在 404
                request.forward(errorPage);
            }
        }
    }

    /**
     * 初始化
     * 获取请求头和post参数串和host信息
     * @throws IOException
     */
    private void init() throws IOException {
        InputStream is = accept.getInputStream();
        byte[] bytes = new byte[1024*1024*1024];
        int length = is.read(bytes);
        String requestStr = new String(bytes,0,length);
        int first_crlf = requestStr.indexOf(CRLF);
        requestHeader = requestStr.substring(0,first_crlf);
        int last_crlf = requestStr.lastIndexOf(CRLF) + CRLF.length();
        postParamStr = requestStr.substring(last_crlf);
    }
}
