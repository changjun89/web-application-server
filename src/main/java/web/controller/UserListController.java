package web.controller;

import webserver.HttpResponse;
import webserver.Request;

import java.util.Map;

import static util.HttpRequestUtils.parseCookies;

public class UserListController extends AbstractController {

    protected void get(Request request , HttpResponse response) throws Exception{
        Map<String, String> cookie = parseCookies(request.getHeaderParam("Cookie"));
        String url = "/user/login.html";
        if ("true".equals(cookie.get("login"))) {
            url = "/user/list.html";
        }
        response.foward(url);
    }
}
