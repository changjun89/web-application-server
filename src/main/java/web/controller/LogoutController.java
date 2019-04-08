package web.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.Request;

public class LogoutController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);


    protected void get(Request request, HttpResponse response) throws Exception {
        request.getHttpSession().removeAttribute("user");
        response.redirect("/user/login.html");
    }


}
