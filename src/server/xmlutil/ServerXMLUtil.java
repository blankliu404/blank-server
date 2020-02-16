package server.xmlutil;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * 工具类实现xml读取 SAX方式
 * @author Blank
 */
public class ServerXMLUtil {
    private ServerXMLUtil() {}

    public static void read(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        ServerHandler handler = ServerHandler.getHandler();
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFile);
        if (resource != null) {
            saxParser.parse(resource, handler);
        }
    }
}
