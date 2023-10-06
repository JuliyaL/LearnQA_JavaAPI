import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Thread.sleep;

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
    @Test
    public void testRestAssuredEx7(){
        System.out.println("Ex7:");
        String url="https://playground.learnqa.ru/api/long_redirect";
        int statusCode=0;
        int count=0;
        while(statusCode!=200)  {
        Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();

            statusCode = response.getStatusCode();
            String locationHeader = response.getHeader("Location");
            if (locationHeader==null) break;
            System.out.println(locationHeader);
            url=locationHeader;
            count++;

        }
        System.out.println("Количество редиректов:"+count);
    }
    @Test
    public void testRestAssuredEx8() throws InterruptedException {
        System.out.println("Ex8:");
        String statusBeforeE="Job is NOT ready";
        String statusAfterE="Job is ready";

        Response response = RestAssured
                .get ("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        String token = response.jsonPath().get("token");
        int seconds = response.jsonPath().get("seconds");

        Response response2 = RestAssured
                .given()
                .queryParam("token",token)
                .get ("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        String statusBefore = response2.jsonPath().get("status");
        if (statusBefore.equals(statusBeforeE)) {
            System.out.println("Тест пройден, получен корректный ответ:"+statusBeforeE);
        } else {
            System.out.println("Тест не пройден, получен некорректный ответ:"+statusBefore);
        }
        long mseconds =1000*seconds+1;
        sleep(mseconds);

        Response response3 = RestAssured
                .given()
                .queryParam("token",token)
                .get ("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        String statusAfter = response3.jsonPath().get("status");
        String result = response3.jsonPath().get("result");

        if (statusAfter.equals(statusAfterE)) {
            System.out.println("Тест пройден, получен корректный ответ:"+statusAfterE);
        } else {
            System.out.println("Тест не пройден, получен некорректный ответ:"+statusAfter);
        }

        if (result!=null) {
            System.out.println("Поле result содержится в ответе:"+result);
        } else {
            System.out.println("Поле result отсутствует в ответе");
        }
    }
    @Test
    public void testRestAssuredEx9(){
        System.out.println("Ex9:");
        String correctLogin = "super_admin";
        String[] passwordsList = {
                "123456",
                "123456789",
                "qwerty",
                "password",
                "1234567",
                "12345678",
                "12345",
                "iloveyou",
                "111111",
                "123123",
                "abc123",
                "qwerty123",
                "1q2w3e4r",
                "admin",
                "qwertyuiop",
                "654321",
                "555555",
                "lovely",
                "7777777",
                "welcome",
                "888888",
                "princess",
                "dragon",
                "password1",
                "123qwe"

        };
        for (int i = 0; i < passwordsList.length; i++) {
            Response response = RestAssured
                    .given()
                    .queryParam("login",correctLogin)
                    .queryParam("password",passwordsList[i])
                    .post(" https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();


            String  responseCookie=response.getCookie("auth_cookie");
            Map<String,String> cookies = new HashMap<>();
            cookies.put("auth_cookie", responseCookie);

            Response responseCheck = RestAssured
                    .given()
                    .queryParam("login",correctLogin)
                    .queryParam("password",passwordsList[i])
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();


            String result = responseCheck.body().asString();
            if (result.equals("You are authorized")) {
                System.out.println("Корректный пароль найден:"+passwordsList[i]);
               break;
            }

        }
          }
    }


