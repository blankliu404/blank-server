package server.xmlutil;

import server.entity.Servlet;
import server.entity.ServletMapping;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例实现Server.xml的读取
 * @author Blank
 */
public class ServerHandler extends DefaultHandler {
    private Map<String, String> servlets;
    private Servlet servlet;
    private Map<String, String> servletMappings;
    private ServletMapping servletMapping;
    private String port;
    private String errorPage;
    private String welcomePage;
    private String tag;

    private static ServerHandler serverHandler;

    private ServerHandler() {}

    public static ServerHandler getHandler() {
        if (serverHandler == null)
            serverHandler = new ServerHandler();
        return serverHandler;
    }

    @Override
    public void startDocument() throws SAXException {
        servlets = new HashMap<>();
        servletMappings = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName != null) {
            tag = qName;
            switch (tag) {
                case "servlet":
                    servlet = new Servlet();
                    break;
                case "servlet-mapping":
                    servletMapping = new ServletMapping();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (tag != null) {
            String content = new String(ch,start,length).trim();
            if (content.length() > 0) {
                switch (tag) {
                    case "name":
                        if (servlet != null)
                            servlet.setName(content);
                        if (servletMapping != null)
                            servletMapping.setName(content);
                        break;
                    case "class":
                        servlet.setClazz(content);
                        break;
                    case "url":
                        servletMapping.setUrl(content);
                        break;
                    case "server-port":
                        port = content;
                        break;
                    case "error-page":
                        errorPage = content;
                        break;
                    case "welcome-page":
                        welcomePage = content;
                        break;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName != null) {
            switch (qName) {
                case "servlet":
                    servlets.put(servlet.getName(),servlet.getClazz());
                    servlet = null;
                    break;
                case "servlet-mapping":
                    servletMappings.put(servletMapping.getUrl(),servletMapping.getName());
                    servletMapping = null;
                    break;
                default:
                    break;
            }
        }
        tag = null;
    }

    public Map<String, String> getServlets() {
        return servlets;
    }

    public Map<String, String> getServletMappings() {
        return servletMappings;
    }

    public String getPort() {
        return port;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public String getWelcomePage() {
        return welcomePage;
    }
}
