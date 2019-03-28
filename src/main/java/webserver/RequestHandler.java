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
                System.out.println("url : " +url);
                httpResponse = new HttpResponse(new DataOutputStream(out));

                if (url.contains(".css")) {
                    httpResponse.foward(url);
                }

                if (url.startsWith("/user/list")) {
                    Map<String, String> cookie = parseCookies(request.getParam("Cookie"));
                    url = "/user/login.html";
                    if ("true".equals(cookie.get("login"))) {
                        url = "/user/list.html";
                    }
                    httpResponse.foward(url);
                }

                if (url.startsWith("/user/login") && !url.startsWith("/user/login.html")) {
                    if (login(request.getParam("userId"), request.getParam("password"))) {
                        httpResponse.addheader("Set-Cookie","login=true");
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

    private String getRequestParamFromUrl(String url) {
        return url.substring(url.indexOf("?") + 1);
    }

    private HashMap<String, String> readLineAndConvertHashMap(BufferedReader br) {
        HashMap<String, String> requestHeader = new HashMap<>();
        try {
            String line;
            while (!"".equals(line = br.readLine())) {
                String[] split = splitString(line);
                if (split.length == 2) {
                    requestHeader.put(split[0], split[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestHeader;
    }

    private Map<String, String> makeRequestParam(String contentBody) {
        return HttpRequestUtils.parseQueryString(contentBody);
    }

    private String[] splitString(String line) {
        return line.split(": ");
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

    private String readFirstLint(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        return br.readLine();
    }

    private User addUser(User user) {
        DataBase.addUser(user);
        log.debug("user 등록 완료 {}", user.toString());
        return user;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: login=true \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

