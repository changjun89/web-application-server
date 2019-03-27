package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class RequestTest {
    Request request;
    InputStream inputStream;

    @Before
    public void setup() throws Exception{
        //File file = new File("C:\\Users\\EDWARD\\IdeaProjects\\web-application-server\\src\\test\\java\\webserver\\requsetTest.txt");
        File file = new File("C:\\Users\\EDWARD\\IdeaProjects\\web-application-server\\src\\test\\java\\webserver\\POST_TEST.txt");
        inputStream = new FileInputStream(file);
        request = new Request(inputStream);
    }

    @Test
    public void headerParsingTest() throws  Exception{
        assertEquals("application/x-www-form-urlencoded",request.getHeaderParam("Content-Type"));
    }

    @Test
    public void 파라미터를_가지고온다() {
        assertEquals("leechang",request.getParam("userId"));
        assertEquals("1234",request.getParam("password"));

    }

}