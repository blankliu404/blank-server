package server;

import org.xml.sax.SAXException;
import server.service.Dispatcher;
import server.xmlutil.ServerHandler;
import server.xmlutil.ServerXMLUtil;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器启动入口
 * @author Blank
 */
public class Server {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        ServerXMLUtil.read("server.xml");
        String port = ServerHandler.getHandler().getPort();
        if (port == null || port.equals(""))
            port = "8888";
        ServerSocket server = new ServerSocket(Integer.parseInt(port));
        System.out.println("___.   .__                 __                                             \n" +
                "\\_ |__ |  | _____    ____ |  | __   ______ ______________  __ ___________ \n" +
                " | __ \\|  | \\__  \\  /    \\|  |/ /  /  ___// __ \\_  __ \\  \\/ // __ \\_  __ \\\n" +
                " | \\_\\ \\  |__/ __ \\|   |  \\    <   \\___ \\\\  ___/|  | \\/\\   /\\  ___/|  | \\/\n" +
                " |___  /____(____  /___|  /__|_ \\ /____  >\\___  >__|    \\_/  \\___  >__|   \n" +
                "     \\/          \\/     \\/     \\/      \\/     \\/                 \\/       \n");
        System.out.println("服务已启动...");
        while (true) {
            Socket accept = server.accept();
            new Dispatcher(accept).start();
        }
    }
}
