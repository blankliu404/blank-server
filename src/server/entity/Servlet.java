package server.entity;

/**
 * servlet实体
 * @author Blank
 */
public class Servlet {
    private String name;
    private String clazz;

    public Servlet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
