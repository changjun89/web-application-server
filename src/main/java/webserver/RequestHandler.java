package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import web.controller.Controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Request request;
    private HttpResponse httpResponse;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            try {
                request = new Request(in);
                httpResponse = new HttpResponse(new DataOutputStream(out));

                if(getSessionId(request.getHeaderParam("Cookie")) == null) {
                    httpResponse.addheader("Set-Cookie","JSESSIONID="+UUID.randomUUID());
                }


                Controller controller = RequestMapping.getController(request.getUrl());
                if (controller != null) {
                    controller.service(request, httpResponse);
                } else {
                    httpResponse.foward(defaultPath(request.getUrl()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private String getSessionId(String cookieValue) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
        return cookies.get("JSESSIONID");
    }

    private String defaultPath(String url) {
        if ("/".equals(url)) {
            return "/index.html";
        }
        return url;
    }


}

