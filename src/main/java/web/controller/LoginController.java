package web.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.Request;
import webserver.RequestHandler;

public class LoginController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    protected void post(Request request, HttpResponse response) throws Exception {
        String url = "/user/login.html";
        if (login(request.getParam("userId"), request.getParam("password"))) {
            //response.addheader("Set-Cookie", "login=true");
            request.getHttpSession().setAttribute("user",DataBase.findUserById(request.getParam("userId")));
            url ="/user/list.html";
        }
        response.redirect(url);
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
}
