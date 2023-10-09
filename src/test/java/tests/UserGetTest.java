package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;

@Epic("Get user cases")
@Feature("Get user")

public class UserGetTest extends BaseTestCase {
    private  final ApiCoreRequests apiCoreRequests=new ApiCoreRequests ();
    @Test
    @Description("This test get user without authorization")
    @DisplayName("Test positive get user without authorization")
    @Severity(SeverityLevel.NORMAL)
    @Story("Positive get user")

    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        System.out.println(responseUserData.asString());

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");

    }
    @Test
    @Description("This test get the same")
    @DisplayName("Test positive. Same user")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Positive get user")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

      /*  Response responseGetAuth = RestAssured
                .given()
                .body (authData)
                .post ("https://playground.learnqa.ru/api/user/login")
                .andReturn();
*/
      Response responseGetAuth = apiCoreRequests
               .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }
    @Test
    @Description("This test authorize one user and get another user")
    @DisplayName("Test positive. Get other user")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Positive get user")
    
    public void testGetOtherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println(responseGetAuth.asString());

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/1", header, cookie);

        System.out.println(responseUserData.asString());

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
}
