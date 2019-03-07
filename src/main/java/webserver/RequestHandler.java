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

import static util.StringUtil.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            log.debug("line cmd : " + line);
            if (line == null) {
                return;
            }
            String url = getUrlCommand(line);
            if (url.startsWith("/user/login") && !url.startsWith("/user/login.html") ) {
                boolean loginResult = false;
                String httpMethod = getHttpMethod(line);
                if ("POST".equals(httpMethod)) {
                    HashMap<String, String> requestHeader = new HashMap<>();
                    Map<String, String> requestParam = new HashMap<>();
                    requestHeader.put("header", line);

                    while (!"".equals(line = br.readLine())) {
                        log.debug("cmd : " + line);
                        String[] split = line.split(": ");
                        if (split.length == 2) {
                            requestHeader.put(split[0], split[1]);
                            continue;
                        }

                    }
                    log.debug("#" + requestHeader.get("Content-Length"));
                    String s = IOUtils.readData(br, Integer.parseInt(requestHeader.get("Content-Length")));
                    requestParam = HttpRequestUtils.parseQueryString(s);
                    User user = DataBase.findUserById(requestParam.get("userId"));
                    if( user != null && user.getPassword().equals(requestParam.get("password"))){
                        log.debug(user.getUserId() + " 님 로그인 성공");
                        loginResult=true;
                    } else {
                        log.debug("로그인 실패");
                    }
                    url = "/index.html";
                } else {
                    int idx = url.indexOf("?");
                    String requestPath = url.substring(0, idx);
                    String requestParam = url.substring(idx + 1);
                    Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(requestParam);
                    User user = new User(parseQueryString.get("userId"), parseQueryString.get("password"), parseQueryString.get("name"), parseQueryString.get("email"));
                    log.debug("user {}", user.toString());
                }
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = getFileContent(url);
                if(loginResult){
                    response200HeaderWithCookie(dos, body.length);
                }else {
                    response200Header(dos, body.length);
                }
                responseBody(dos, body);
            }

            if (url.startsWith("/user/create")) {
                String httpMethod = getHttpMethod(line);
                if ("POST".equals(httpMethod)) {
                    HashMap<String, String> requestHeader = new HashMap<>();
                    Map<String, String> requestParam = new HashMap<>();
                    requestHeader.put("header", line);

                    while (!"".equals(line = br.readLine())) {
                        log.debug("cmd : " + line);
                        String[] split = line.split(": ");
                        if (split.length == 2) {
                            requestHeader.put(split[0], split[1]);
                            continue;
                        }

                    }
                    log.debug("#" + requestHeader.get("Content-Length"));
                    String s = IOUtils.readData(br, Integer.parseInt(requestHeader.get("Content-Length")));
                    requestParam = HttpRequestUtils.parseQueryString(s);

                    User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));
                    DataBase.addUser(user);
                    log.debug("user 등록 완료 {}", user.toString());
                    url = "/index.html";
                } else {
                    int idx = url.indexOf("?");
                    String requestPath = url.substring(0, idx);
                    String requestParam = url.substring(idx + 1);
                    Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(requestParam);
                    User user = new User(parseQueryString.get("userId"), parseQueryString.get("password"), parseQueryString.get("name"), parseQueryString.get("email"));
                    DataBase.addUser(user);
                    log.debug("user 등록 완료 {}", user.toString());
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

    private String readFirstLint(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        return br.readLine();
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

