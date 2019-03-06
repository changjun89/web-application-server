package util;

import model.User;
import org.junit.Test;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static util.StringUtil.getUrlCommand;

public class StringUtilTest {

    @Test
    public void url명령어를_찾는다() {
        String arg = "GET /index.html HTTP/1.1";
        assertEquals("/index.html", getUrlCommand(arg));
    }

    @Test
    public void 요청url과_파라미터를_분리한다() {
        String arg = "/user/create?userId=leechang&password=1234&name=%EC%9D%B4%EC%B0%BD%EC%A4%80&email=leechang9751%40gmail.com";
        int idx = arg.indexOf("?");
        assertEquals("/user/create",arg.substring(0,idx));
        assertEquals("userId=leechang&password=1234&name=%EC%9D%B4%EC%B0%BD%EC%A4%80&email=leechang9751%40gmail.com",arg.substring(idx+1));

    }

    @Test
    public void 쿼리_파람_정상_확인() {
        String cmd = "userId=leechang&password=1234&name=%EC%9D%B4%EC%B0%BD%EC%A4%80&email=leechang9751%40gmail.com";
        Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(cmd);
        User user = new User(parseQueryString.get("userId"),parseQueryString.get("password"),parseQueryString.get("name"),parseQueryString.get("email"));

        assertEquals("leechang",user.getUserId());
        assertEquals("1234",user.getPassword());
        assertEquals("%EC%9D%B4%EC%B0%BD%EC%A4%80",user.getName());
        assertEquals("leechang9751%40gmail.com",user.getEmail());
    }

}