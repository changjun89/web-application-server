package webserver;

import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Request {

    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private HashMap<String, String> headerInfo;
    private RequestHeader requestHeader;
    private RequestURL requestURL;
    private RequestParam requestParam;

    public Request(InputStream inputStream) throws Exception {

        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        createRequestURL();
        createRequestHeader();

        if ("POST".equals(requestURL.getHttpMethod())) {
            String queryString = IOUtils.readData(bufferedReader, Integer.parseInt(requestHeader.getParam("Content-Length")));
                    System.out.println("queryString : " + queryString);
            requestParam = new RequestParam(queryString);
        }
        if ("GET".equals(requestURL.getHttpMethod())) {
            String queryString = requestURL.getQueryParam();
            System.out.println("queryString : " + queryString);
            requestParam = new RequestParam(queryString);
        }
    }

    public String getParam(String key) {
        return requestParam.getParam(key);
    }

    public void addParam(String key, String value) {
        requestParam.addParam(key, value);
    }

    private String readLine() throws Exception {
        String line = bufferedReader.readLine();
        if (line == null) {
            throw new Exception();
        }
        return line;
    }

    private void createRequestURL() throws Exception {
        requestURL = new RequestURL(readLine());
    }

    private String getHttpMethod(String cmd) {
        return cmd.split(" ")[0];
    }

    public String getHeaderParam(String key) {
        return requestHeader.getParam(key);
    }

    public String getHttpMethod() {
        return requestURL.getHttpMethod();
    }

    public String getHttpVersioin() {
        return requestURL.getHttpVersion();
    }

    public String getUrl() {
        return requestURL.getUrl();
    }

    private RequestHeader createRequestHeader() throws IOException {
        requestHeader = new RequestHeader();
        String line;
        while (!"".equals(line = bufferedReader.readLine())) {
            System.out.println("line : " + line);
            //while ((line = bufferedReader.readLine()) != null) {
            String[] split = splitString(line);
            if (split.length == 2) {
                requestHeader.addParam(split[0], split[1]);
            }
        }
        return requestHeader;
    }

    private String[] splitString(String line) {
        return line.split(": ");
    }


}
