import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
