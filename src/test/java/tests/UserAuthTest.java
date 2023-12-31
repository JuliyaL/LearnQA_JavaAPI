package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;

   private  final ApiCoreRequests apiCoreRequests=new ApiCoreRequests ();
    @BeforeEach
    public void loginUser(){
        Map<String,String> authData=new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = apiCoreRequests
                .makePostRequest
                        ("https://playground.learnqa.ru/api/user/login", authData);
/*
        Response responseGetAuth= RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
*/
        this.cookie=this.getCookie(responseGetAuth,"auth_sid");
        this.header=this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth=this.getIntFromJson(responseGetAuth,"user_id");
       }
    @Description("This test check authorization status without sending cookie or token")
    @DisplayName("Test negative auth user")
    @Severity(SeverityLevel.MINOR)
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    @Story("Negative auth user")

    public void testNegativeAuthUser(String condition) {
/*
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
*/
        if(condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests
                    .makeGetRequestWithCookie("https://playground.learnqa.ru/api/user/auth", this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        }
        else if(condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests
                    .makeGetRequestWithToken("https://playground.learnqa.ru/api/user/auth", this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        }
        else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }

/*
        RequestSpecification spec=RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid",this.cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token",this.header); }
        else {
            throw new IllegalArgumentException("Condition value is not known:"+condition);
        }
        Response responseForCheck=spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck,"user_id",0);

 */
    }
       @Test
       @Description("This test successfully authorize user by email and password")
       @DisplayName("Test positive auth user")
       @Severity(SeverityLevel.CRITICAL)
       @Story("Positive auth user")

    public void testAuthUser(){
      /* Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

       */
           Response responseCheckAuth = apiCoreRequests
                   .makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                                        this.header, this.cookie);

           Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

        }



