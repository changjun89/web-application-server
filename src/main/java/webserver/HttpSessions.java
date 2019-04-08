package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static Map<String, HttpSession> sessionMap = new HashMap<>();

    public static HttpSession getSession(String key) {
        HttpSession session = sessionMap.get(key);
        if (session == null) {
            session = new HttpSession(key);
            sessionMap.put(key,session);
        }
        return session;
    }

    static void remove(String key) {
        sessionMap.remove(key);
    }

}
