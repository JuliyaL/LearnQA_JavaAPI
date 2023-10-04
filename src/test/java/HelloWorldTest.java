import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HelloWorldTest {
    @Test
    public void testHelloWorld (){
   System.out.println("Hello from Juliya!");
    Response response = RestAssured
            .get ("https://playground.learnqa.ru/api/get_text")
        .andReturn();
    response.prettyPrint();
            }
}
