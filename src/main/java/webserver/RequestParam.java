package webserver;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestParam {

    private Map<String ,String> paramMap ;

    public RequestParam(String queryParam) {
        paramMap = HttpRequestUtils.parseQueryString(queryParam);
    }

    public String getParam(String key) {
        return paramMap.get(key);
    }

    public Map<String,String> addParam(String key,String value){
        paramMap.put(key,value);
        return paramMap;
    }
}
