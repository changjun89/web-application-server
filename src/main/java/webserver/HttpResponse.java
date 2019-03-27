package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static util.StringUtil.getFileContent;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    DataOutputStream dos;
    HashMap<String,String> headers = new HashMap<>();

    public HttpResponse(DataOutputStream dataOutputStream) {
        this.dos = dataOutputStream;
    }

    public void foward(String url) throws Exception {
        byte[] body = getFileContent(url);

        if (url.endsWith(".css")) {
            headers.put("Content-Type", "text/css");
        } else if (url.endsWith(".js")) {
            headers.put("Content-Type", "application/javascript");
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
        }
        headers.put("Content-Length", body.length + "");
        response200Header(body.length);
        responseBody(body);
    }
    public void redirect(String url) {

        response302Header(url);
    }
    public void forwardBody(String body) {
        byte[] contents = body.getBytes();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", contents.length + "");
        response200Header(contents.length);
        responseBody(contents);
    }


    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            makeResponse();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void makeResponse() {
        Set<String> headerKey = headers.keySet();
        Iterator<String> iterator = headerKey.iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            try {
                dos.writeBytes(key+": "+headers.get("key")+"\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void response200HeaderWithCookie(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.flush();
            dos.writeBytes("Set-Cookie: login=true \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
