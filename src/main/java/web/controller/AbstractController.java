package web.controller;

import webserver.HttpResponse;
import webserver.Request;

public abstract class AbstractController implements Controller{

   public void service(Request request , HttpResponse response) throws Exception{
       if("POST".equals(request.getHttpMethod())) {
           post(request,response);
       }else{
           get(request,response);
       }
   }

    protected void get(Request request, HttpResponse response) throws Exception{

    };


    protected void post(Request request, HttpResponse response) throws Exception{

    };
}
