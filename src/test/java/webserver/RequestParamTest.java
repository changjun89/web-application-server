package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestParamTest {

    private static final String queryString = "userId=leechang&password=1234";
    RequestParam requestParam;

    @Before
    public void setup() {
        requestParam = new RequestParam(queryString);
    }

    @Test
    public void 파라미터를가지고온다() {
        assertEquals("leechang",requestParam.getParam("userId"));
        assertEquals("1234",requestParam.getParam("password"));
    }

}