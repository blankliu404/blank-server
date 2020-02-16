package server.service;

import java.io.IOException;

/**
 * 模仿Tomcat的HttpResponse的部分功能
 * @author Blank
 */
public class Response {

    /**
     * 重定向到页面
     * @param path
     */
    public void redirect(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>")
                .append("location.href = '")
                .append(path)
                .append("';")
                .append("</script>");
        // 封装信息
        Request.pushInfo(sb.toString(),200);
    }


}
