package webserver;

import web.controller.*;

import java.util.HashMap;

public class RequestMapping {

    private static HashMap<String, Controller> controllerMap = new HashMap<String, Controller>();

    static {
        controllerMap.put("/user/create", new UserCreateController());
        controllerMap.put("/user/list", new UserListController());
        controllerMap.put("/user/login", new LoginController());
        controllerMap.put("/user/logout", new LogoutController());
    }

    public static Controller getController(String request) {
        return controllerMap.get(request);
    }
}
