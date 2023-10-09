package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
@Epic("User registration cases")
@Feature("Registration")

public class UserRegisterTest extends BaseTestCase {
    private  final ApiCoreRequests apiCoreRequests=new ApiCoreRequests ();
    @Test
    @Description("This test try to register user with existing email")
    @DisplayName("Test negative registration. Existing email")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative register user")

    public void testCreateUserWithExistingEmail() {
    String email = "vinkotov@example.com";

    Map<String, String> userData = new HashMap<>();
      userData.put("email", email);
      userData = DataGenerator.getRegistrationData(userData);

    /*Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();
*/
    Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }
    @Test
    @Description("This test successfully register user")
    @DisplayName("Test positive registration")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Positive register user")

    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
            }
    @Test
    @Description("This test try to register user with invalid email")
    @DisplayName("Test negative registration. Invalid email")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative register user")

    public void testCreateUserWithInvalidEmail() {
        Map<String, String> invalidEmail = new HashMap<>();
        invalidEmail.put("email", DataGenerator.getRandomInvalidEmail());
        Map<String, String> userData = DataGenerator.getRegistrationData(invalidEmail);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

       // System.out.println(userData);
       // System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Description("This test try to register user without one of field in userData")
    @DisplayName("Test negative registration. Invalid set of fields")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative register user")

    public void testCreateUserWithInvalidSetOfFields(String fieldkey) {
        Map<String, String> userData = DataGenerator.getRegistrationDataWithoutOneField(fieldkey);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //System.out.println(userData);
        //System.out.println(responseCreateAuth.asString());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + fieldkey);
    }

    @Description("This test try to register user with short name")
    @DisplayName("Test negative registration. Too short name")
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName"})
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289481")
    @Story("Negative register user")

    public void testCreateUserWithTooShortName(String name) {
        Map<String, String> userData = DataGenerator.getRegistrationDataWithShortName(name);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //System.out.println(userData);
        //System.out.println(responseCreateAuth.asString());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + name + "' field is too short");
    }

    @Description("This test try to register user with too long name")
    @DisplayName("Test negative registration. Long name")
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName"})
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289481")
    @Story("Negative register user")

    public void testCreateUserWithTooLongName(String name) {
        Map<String, String> userData = DataGenerator.getRegistrationDataWithLongName(name);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

       // System.out.println(userData);
       // System.out.println(responseCreateAuth.asString());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + name + "' field is too long");
    }
}
