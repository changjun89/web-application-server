package web.controller;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.Request;

public class UserCreateController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    protected void post(Request request, HttpResponse response) throws Exception {
        User user = new User(request.getParam("userId"), request.getParam("password"), request.getParam("name"), request.getParam("email"));
        addUser(user);
        response.redirect("/index.html");
    }

    private User addUser(User user) {
        DataBase.addUser(user);
        log.debug("user 등록 완료 {}", user.toString());
        return user;
    }
}
