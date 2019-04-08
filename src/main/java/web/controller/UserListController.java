package web.controller;

import webserver.HttpResponse;
import webserver.Request;

import java.util.Map;

import static util.HttpRequestUtils.parseCookies;

public class UserListController extends AbstractController {

    protected void get(Request request , HttpResponse response) throws Exception{
        //Map<String, String> cookie = parseCookies(request.getHeaderParam("Cookie"));
        String url = "/user/list.html";
        if(request.getHttpSession().getAttribute("user") ==null) {
            url = "/user/login.html";
        }
        response.foward(url);
    }
}
