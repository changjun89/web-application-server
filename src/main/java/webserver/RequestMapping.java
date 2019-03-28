package webserver;

import web.controller.Controller;
import web.controller.LoginController;
import web.controller.UserCreateController;
import web.controller.UserListController;

import java.util.HashMap;

public class RequestMapping {

    private static HashMap<String, Controller> controllerMap = new HashMap<String, Controller>();

    static {
        controllerMap.put("/user/create", new UserCreateController());
        controllerMap.put("/user/list", new UserListController());
        controllerMap.put("/user/login", new LoginController());
    }

    public static Controller getController(String request) {
        return controllerMap.get(request);
    }
}
