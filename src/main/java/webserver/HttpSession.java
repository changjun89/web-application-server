package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private String id;
    private Map<String,Object> sessionAttribute = new HashMap<>();

    HttpSession(String UUID) {
        this.id = UUID;
    }

    public String getId() {
        return id;
    }

    public void setAttribute( String key,Object object) {
        sessionAttribute.put(key,object);
    }

    public Object getAttribute(String key) {
        return sessionAttribute.get(key);
    }

    public void removeAttribute(String key) {
        sessionAttribute.remove(key);
    }

    public void invalidate() {
        sessionAttribute.clear();
    }
}
