package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class HttpResponseTest {

    HttpResponse httpResponse;
    static final String filePath = "C:\\Users\\EDWARD\\IdeaProjects\\web-application-server\\src\\test\\java\\webserver\\";
    @Before
    public void setup() throws FileNotFoundException {

    }


    @Test
    public void forwardTest() throws Exception {
        httpResponse = new HttpResponse(createOutputStream("Http_Forward.txt"));
        httpResponse.foward("/index.html");
    }

    @Test
    public void redirectTest() throws Exception {
        httpResponse = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        httpResponse.redirect("/index.html");
    }
    @Test
    public void responseCookies() throws Exception {
        httpResponse = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        httpResponse.addheader("Set-Cookie", "logined=true");
        httpResponse.redirect("/index.html");
    }



    private DataOutputStream createOutputStream(String fileName) throws FileNotFoundException {
            return new DataOutputStream(new FileOutputStream(filePath+fileName));

    }



}