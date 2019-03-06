package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public  class  StringUtil {

    public static String getUrlCommand(String cmd) {
        return cmd.split(" ")[1];
    }
    public static String getHttpMethod(String cmd) {
        return cmd.split(" ")[0];
    }

    public static byte[] getFileContent(String url){
        try {
            return Files.readAllBytes(new File("./webapp"+url).toPath());
        } catch (IOException e) {
            return  "hello changjun".getBytes();
        }
    }
}
