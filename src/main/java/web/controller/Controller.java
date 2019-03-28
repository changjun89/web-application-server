package web.controller;

import webserver.HttpResponse;
import webserver.Request;

public interface Controller {
    public void service(Request request, HttpResponse response)throws Exception;
}
