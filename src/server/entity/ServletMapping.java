package server.entity;

/**
 * servlet-mapping实体
 * @author Blank
 */
public class ServletMapping {
    private String name;
    private String url;

    public ServletMapping() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
