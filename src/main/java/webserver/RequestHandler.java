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

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            log.debug("line cmd : " + line);

            if (line == null) {
                return;
            }

            String url = getUrlCommand(line);

            if (url.contains(".css")) {
                byte[] body = getFileContent(url);
                DataOutputStream dos = new DataOutputStream(out);
                response200HeaderCss(dos, body.length);
                responseBody(dos, body);
            }


            if (url.startsWith("/user/list")) {
                HashMap<String, String> requestHeader = readLineAndConvertHashMap(br);
                requestHeader.put("header", line);
                Map<String, String> cookie = parseCookies(requestHeader.get("Cookie"));
                url = "/user/login.html";
                if ("true".equals(cookie.get("login"))) {
                    url = "/user/list.html";
                }
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = getFileContent(url);
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

            if (url.startsWith("/user/login") && !url.startsWith("/user/login.html")) {
                boolean loginResult = false;
                if ("POST".equals(getHttpMethod(line))) {
                    HashMap<String, String> requestHeader = readLineAndConvertHashMap(br);
                    String content = IOUtils.readData(br, Integer.parseInt(requestHeader.get("Content-Length")));
                    Map<String, String> requestParam = makeRequestParam(content);
                    loginResult = login(requestParam.get("userId"), requestParam.get("password"));
                    url = "/index.html";
                }

                if ("GET".equals(getHttpMethod(line))) {
                    Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(getRequestParamFromUrl(url));
                    loginResult = login(parseQueryString.get("userId"), parseQueryString.get("password"));
                }

                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = getFileContent(url);
                if (loginResult) {
                    response200HeaderWithCookie(dos, body.length);
                } else {
                    response200Header(dos, body.length);
                }
                responseBody(dos, body);
            }

            if (url.startsWith("/user/create")) {
                String httpMethod = getHttpMethod(line);
                if ("POST".equals(httpMethod)) {
                    HashMap<String, String> requestHeader = readLineAndConvertHashMap(br);
                    String content = IOUtils.readData(br, Integer.parseInt(requestHeader.get("Content-Length")));
                    createUser(makeRequestParam(content));
                    url = "/index.html";
                }

                if ("GET".equals(getHttpMethod(line))) {
                    Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(getRequestParamFromUrl(url));
                    createUser(parseQueryString);
                }

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
                byte[] body = getFileContent(url);
                responseBody(dos, body);

            }

            byte[] body = getFileContent(url);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch (IOException e) {
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

    private User createUser(Map<String, String> requestParam) {
        User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));
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

