package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static util.StringUtil.getUrlCommand;

public class StringUtilTest {

    @Test
    public void url명령어를_찾는다() {
        String arg = "GET /index.html HTTP/1.1";
        assertEquals("/index.html", getUrlCommand(arg));
    }

}