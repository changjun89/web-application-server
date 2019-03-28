package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.parseCookies;
import static util.StringUtil.*;

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
                String url = request.getUrl();
                System.out.println("url : " + url);
                httpResponse = new HttpResponse(new DataOutputStream(out));

                if (url.contains(".css")) {
                    httpResponse.foward(url);
                }

                if (url.startsWith("/user/list")) {
                    Map<String, String> cookie = parseCookies(request.getHeaderParam("Cookie"));
                    url = "/user/login.html";
                    if ("true".equals(cookie.get("login"))) {
                        url = "/user/list.html";
                    }
                    httpResponse.foward(url);
                }

                if (url.startsWith("/user/login") && !url.startsWith("/user/login.html")) {
                    if (login(request.getParam("userId"), request.getParam("password"))) {
                        httpResponse.addheader("Set-Cookie", "login=true");
                    }
                    httpResponse.foward(url);
                }

                if (url.startsWith("/user/create")) {
                    User user = new User(request.getParam("userId"), request.getParam("password"), request.getParam("name"), request.getParam("email"));
                    addUser(user);
                    httpResponse.redirect("/index.html");
                }

                httpResponse.foward(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private boolean login(String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (user != null && user.getPassword().equals(password)) {
            log.debug(user.getUserId() + " 님 로그인 성공");
            return true;
        }
        log.debug("로그인 실패");
        return false;
    }

    private User addUser(User user) {
        DataBase.addUser(user);
        log.debug("user 등록 완료 {}", user.toString());
        return user;
    }

}

