package server.service;

import server.xmlutil.ServerHandler;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

/**
 * Request
 * @author Blank
 */
public class Request {
    // 常量
    private static final String CRLF = "\r\n";
    private static final String SP = " ";

    private static Socket accept;

    private static String method;
    private static String url;
    private static String route;
    private static String protocol;

    private Map<String, Set<String>> params;

    // 默认code 200
    private int code = 200;
    private static Map<Integer,String> codeInfo;

    static {
        codeInfo = new HashMap<>();
        // 成功
        codeInfo.put(200,"OK");
        // 资源未找到
        codeInfo.put(404,"Not Found");
        // 服务器错误
        codeInfo.put(500,"Internal Server Error");
        // 重定向
        codeInfo.put(300,"Multiple Choices");

    }

    public Request(Socket accept,String requestHeader,String postParamStr) {
        this.accept = accept;
        params = new HashMap<>();
        String[] info = requestHeader.split(SP);
        method = info[0];
        url = info[1];
        protocol = info[2];
        // 获取 route 和 param
        if (method.equals("GET")) {
            if (url.contains("?")) {
                // 有参数
                int i = url.indexOf("?");
                route = url.substring(0,i);
                dealParam(url.substring(i+1));
            }else {
                // 无参数
                route = url;
            }
        }else if (method.equals("POST")) {
            route = url;
            dealParam(postParamStr);
        }
    }

    /**
     * 处理单参数
     * @param p
     */
    private void dealSingleParam(String p) {
        // 单参数
        if (p.contains("=")) {
            // 可分割
            String[] temp = p.split("=");
            if (temp.length > 0) {
                if (!params.containsKey(temp[0])) {
                    params.put(temp[0],new HashSet<>());
                }
                if (temp.length < 2) {
                    // 处理特殊如 name=
                    params.get(temp[0]).add("");
                }else {
                    params.get(temp[0]).add(temp[1]);
                }
            }
        }else {
            // 不可分割 给值赋""
            if (!params.containsKey(p)) {
                params.put(p,new HashSet<>());
            }
            params.get(p).add("");
        }
    }

    /**
     * 处理多参数
     * @param paramStr
     */
    private void dealParam(String paramStr) {
        // 有参数
        if (!paramStr.equals("")) {
            if (paramStr.contains("&")) {
                // 多参数
                String[] p = paramStr.split("&");
                for (String s : p) {
                    // 进行单参数处理
                    dealSingleParam(s);
                }
            }else {
                // 单参数
                dealSingleParam(paramStr);
            }
        }
    }

    /**
     * 转发到页面
     * @param path
     */
    public void forward(String path) throws IOException {
        // 获取path的resource
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        String content = "";
        if (resource == null) {
            code = 404;
            path = ServerHandler.getHandler().getErrorPage();
            resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        }

        StringBuilder sbbody = new StringBuilder();
        // 读取文件
        byte[] bytes = new byte[1024*1024];
        int length;
        while ((length = resource.read(bytes)) != -1) {
            sbbody.append(new String(bytes,0,length));
        }
        content = sbbody.toString();

        // 封装请求头
        pushInfo(content, code);
    }

    /**
     * 获取参数单值
     * @param key
     * @return
     */
    public String getParameter(String key) throws UnsupportedEncodingException {
        String value = null;
        if (params.containsKey(key)) {
            Set<String> strings = params.get(key);
            Object[] values = strings.toArray();
            if (values.length == 1)
                value = values[0].toString();
        }
        return URLDecoder.decode(value,"UTF-8");
    }

    /**
     * 获取参数多值
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) throws UnsupportedEncodingException {
        String[] retValues = null;
        if (params.containsKey(key)) {
            Set<String> strings = params.get(key);
            Object[] values = strings.toArray();
            if (values.length > 1) {
                retValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    retValues[i] = URLDecoder.decode(values[i].toString(),"UTF-8");
                }
            }
        }
        return retValues;
    }

    public static void pushInfo(String content, int code) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(SP)
                .append(code).append(SP)
                .append(codeInfo.get(code))
                .append(CRLF);
        sb.append("Server:")
                .append("blank/0.0.1")
                .append(CRLF);
        sb.append("Date:")
                .append(new Date())
                .append(CRLF);
        sb.append("Content-Type:")
                .append("text/html")
                .append(CRLF);
        sb.append("Content-Length:")
                .append(content.getBytes().length)
                .append(CRLF);
        sb.append(CRLF).append(content)
                .append(CRLF);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    public String getRoute() {
        return route;
    }

    public String getMethod() {
        return method;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<Integer, String> getCodeInfo() {
        return codeInfo;
    }
}
