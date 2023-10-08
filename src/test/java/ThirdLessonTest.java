import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ThirdLessonTest {
    @ParameterizedTest
    @ValueSource(strings = {"Осень", "Осень-это сны листопада..."})
    public void Ex10(String strExample){
        int strLength = strExample.length();
        assertTrue(strLength >15, "Тест провален. Длина строки меньше 15 символов:"+strExample);
    }
    @Test
    public void Ex11(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        String cookie = response.getCookies().toString();
        //System.out.println("cookie:"+cookie);
        assertEquals("{HomeWork=hw_value}", cookie, "Неправильные cookie:"+cookie);
    }

    @Test
    public void Ex12(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        //System.out.println(response.getHeaders().toString());
        String header = response.getHeader("x-secret-homework-header");
        assertEquals("Some secret value", header, "Неправильный заголовок:"+header);
    }
    @ParameterizedTest
    @CsvSource(value = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30*Mobile*No*Android",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1*Mobile*Chrome*iOS",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)*Googlebot*Unknown*Unknown",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0*Web*Chrome*No",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1*Mobile*No*iPhone"
    },delimiter = '*', ignoreLeadingAndTrailingWhitespace = true)

        public void Ex13(String userAgentEx,String platformEx,String browserEx,String deviceEx){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("User-Agent", userAgentEx);

        JsonPath response = RestAssured
                .given()
                .headers(queryParams)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        //response.prettyPrint();

        String userAgent = response.getString("user_agent");
        String platform = response.getString("platform");
        String browser = response.getString("browser");
        String device = response.getString("device");

        System.out.println(userAgent);

        assertAll(userAgent,
        () ->assertEquals(platformEx,platform,"Некорректное значение platform!"),
        () ->assertEquals(browserEx,browser,"Некорректное значение browser!"),
        () ->assertEquals(deviceEx,device,"Некорректное значение device!")
        );
    }
        }


