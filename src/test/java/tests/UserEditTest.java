package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import java.util.HashMap;
import java.util.Map;
import io.qameta.allure.*;

import static org.junit.jupiter.api.Assertions.assertAll;

@Epic("User Edit cases")
@Feature("User edit")

public class UserEditTest extends BaseTestCase {
     private  final ApiCoreRequests apiCoreRequests=new ApiCoreRequests ();

    @Test
    @Description("This test successfully create and edit user")
    @DisplayName("Test positive edit user")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Positive edit user")

    public void testEditJustCreatedTest() {
    //Generate user
    Map<String, String> userData = DataGenerator.getRegistrationData();

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", userData);

    String userId = responseCreateAuth.getString("id");

    //login
    Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    //edit
    String newName = "Changed name";
    Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
            this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
    editData
        );
    //GET
    Response responseUserData = apiCoreRequests
            .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                    this.getHeader(responseGetAuth, "x-csrf-token"),
                    this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
}
    @Test
    @Description("This test try to edit user whithout authorization")
    @DisplayName("Test negative edit user. Not Authorized")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative edit user")

    public void testEditNotAuthTest() {

        //edit
        String newName = "New name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestNotAuth(
                "https://playground.learnqa.ru/api/user/2",
                editData
        );
       // System.out.println(responseEditUser.asString());

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");

    }

    @Test
    @Description("This test successfully create user and try to edit (invalid) email")
    @DisplayName("Test negative edit user. Invalid email")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative edit user")

    public void testEditWithInvalidEmail() {
        //Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //login
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //edit
        String newEmail = DataGenerator.getRandomInvalidEmail();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );

        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    @Description("This test create user and try to edit user with invalid (too shoot) firstName")
    @DisplayName("Test negative edit user. Invalid firstName")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative edit user")

    public void testEditWithInvalidFirstName() {
        //Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //login
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //edit
        String newFirstName = "l";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );
       // System.out.println(responseEditUser.asString());
        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Too short value for field firstName\"}");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }
    @Test
    @Description("This test successfully create,login one user and try to edit other user")
    @DisplayName("Test negative. Edit other user")
    @Story("Negative edit user")
    @Severity(SeverityLevel.NORMAL)

        public void testEditOtherUser() {
       //создание 1го нового пользователя
        Map<String, String> firstUserData = DataGenerator.getRegistrationData();
        String firstUserName = firstUserData.get("firstName");

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", firstUserData);

        String firstUserId = responseCreateAuth.getString("id");
       // System.out.println("firstUserName:"+firstUserName);

        //создание 2 пользователя
        Map<String, String> secondUserData = DataGenerator.getRegistrationData();
        String secondUserName = secondUserData.get("firstName");
       // System.out.println("secondUserName:"+secondUserName);

        JsonPath responseCreateUserForEdit = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", secondUserData);

        String secondUserId = responseCreateUserForEdit.getString("id");

        //Логин под первым юзером
        Map<String,String> authData = new HashMap<>();
        authData.put("email", firstUserData.get("email"));
        authData.put("password", firstUserData.get("password"));

       Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //редактирование имени второго пользователя из-под первого

        String newName = "New coolName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + secondUserId,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );

        //получаем данные залогин.пользователя для проверки изменения имени первого пользователя
        Response responseFirstUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + firstUserId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //System.out.println("responseFirstUserData:"+responseFirstUserData.asString());

        //получаем данные второго пользователя для проверки изменения имени пользователя
        Response responseSecondUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + secondUserId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //System.out.println("responseSecondUserData:"+responseFirstUserData.asString());

        //тест провальный, ожидаемое поведение:метод выдаст ошибку (не будет выполнено редактирование данных)
        //фактическое: изменились имена у обоих пользователей

        assertAll("Сложный сценарий проверки операции редактирования",
                () -> Assertions.assertResponseCodeEquals(responseEditUser, 400),
                () -> Assertions.assertJsonByName(responseSecondUserData, "firstName",secondUserName),
                () -> Assertions.assertJsonByName(responseFirstUserData, "firstName",firstUserName)

        );


    }

}
