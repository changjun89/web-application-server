package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestURLTest {

    static final String requestSample = "GET /user/login?userId=leechang&password=1234 HTTP/1.1";
    private RequestURL requestURL;
    @Before
    public void setup() {
        requestURL = new RequestURL(requestSample);
    }

    @Test
    public void HTTP메소스들_가지고온다() {
        assertEquals("GET",requestURL.getHttpMethod());
    }

    @Test
    public void url을가지고온다() {
        assertEquals("/user/login",requestURL.getUrl());
    }

    @Test
    public void http버전을가지고온다() {
        assertEquals("HTTP/1.1",requestURL.getHttpVersion());
    }


}