import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class SecondLessonTest {
    @Test
    public void testRestAssuredEx5(){
        System.out.println("Ex5:");
        Response response = RestAssured
                .get ("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();
       System.out.println("Первый вариант с помощью Map:");
       Map<String, String> message = response.jsonPath().getMap("messages[1]");
       System.out.println(message.get("message"));

       System.out.println("Второй вариант с помощью List:");
       List<Map<String, String>> messages = response.jsonPath().getList("messages");
       System.out.println(messages.get(1).get("message"));

    }

    @Test
    public void testRestAssuredEx6(){
        System.out.println("Ex6:");
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get ("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader=response.getHeader("Location");
        System.out.println(locationHeader);
            }

}
