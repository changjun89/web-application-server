package webserver;

import java.util.HashMap;

public class RequestURL {
    private String requsetURL;
    private String method;
    private String url;
    private String fullUrl;
    private String httpVersion;
    private String queryParam;

    public RequestURL(String requestURL) {
        this.requsetURL = requestURL;
        this.method = requsetURL.split(" ")[0];
        this.fullUrl = requsetURL.split(" ")[1];
        this.httpVersion = requsetURL.split(" ")[2];
        setUrl();
    }

    private void setUrl() {
        this.url = fullUrl.contains("?") ? parsingUrl() : fullUrl;
    }

    public String getHttpMethod() {
        return method;
    }

    private String parsingUrl() {
        this.queryParam = fullUrl.substring(fullUrl.indexOf("?") + 1);
        return fullUrl.substring(0, fullUrl.indexOf("?"));
    }


    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getQueryParam() {
        return queryParam;
    }
}
