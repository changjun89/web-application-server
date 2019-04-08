package webserver;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class Cookies {
    private Map<String, String> cookie;
    private final static String JSESSIONID = "JSESSIONID";

    Cookies(String queryString) {
        cookie = HttpRequestUtils.parseCookies(queryString);
    }

    public String get(String key) {
        return cookie.get(key);
    }

    public boolean containJSESSIONID() {
        return cookie.containsValue(JSESSIONID);
    }
    public String getJsessionid () {
        return cookie.get(JSESSIONID);
    }
}
