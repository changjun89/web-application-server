package webserver;

import java.util.HashMap;

public class RequestHeader {

    private HashMap<String,String> header = new HashMap<>();

    public void addParam(String key,String value) {
        header.put(key, value);
    }
    public String getParam(String Key) {
        return header.get(Key);
    }
}
