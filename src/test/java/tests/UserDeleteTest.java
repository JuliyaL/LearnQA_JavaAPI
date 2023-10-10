package tests;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.DataGenerator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;

@Epic("User Delete cases")
@Feature("User delete")

public class UserDeleteTest extends BaseTestCase {
    private  final ApiCoreRequests apiCoreRequests=new ApiCoreRequests ();
    @Test
    @Description("This test try to delete protected user by id: 2")
    @DisplayName("Test negative. Delete protected user")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative delete user")
    @Owner("JuliyaL")

    public void testDeleteProtectedUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest
                        ("https://playground.learnqa.ru/api/user/2",
                                responseGetAuth.getHeader("x-csrf-token"),
                                responseGetAuth.getCookie("auth_sid")
                        );

        //System.out.println(responseDeleteUser.asString());
        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }
    @Test
    @Description("This test successfully create user and delete then")
    @DisplayName("Test positive delete user")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Positive delete user")
    @Owner("JuliyaL")

    public void testDeleteUser() {
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

        //delete user
        Response responseDeleteUserData =apiCoreRequests                 .makeDeleteRequest
                        ("https://playground.learnqa.ru/api/user/" + userId,
                                responseGetAuth.getHeader("x-csrf-token"),
                                responseGetAuth.getCookie("auth_sid")
                        );

        Assertions.assertResponseCodeEquals(responseDeleteUserData, 200);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        System.out.println(responseUserData.asString());
        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }
    @Test
    @Description("This test create user, create second user and delete")
    @DisplayName("Test negative. Delete other user")
    @Severity(SeverityLevel.NORMAL)
    @Story("Negative delete user")
    @Owner("JuliyaL")

    public void testDeleteOtherUser() {
        //Generate firstUser
        Map<String, String> firstUserData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", firstUserData);

        String firstUserId = responseCreateAuth.getString("id");

        //login by firstUser
        Map<String,String> authData = new HashMap<>();
        authData.put("email", firstUserData.get("email"));
        authData.put("password", firstUserData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //Generate secondUser for delete
        Map<String, String> SecondUserData = DataGenerator.getRegistrationData();

        JsonPath responseCreateUserForDelete = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", SecondUserData);

        String secondUserId = responseCreateUserForDelete.getString("id");


        //delete secondUserId
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest
                        ("https://playground.learnqa.ru/api/user/" + secondUserId,
                                responseGetAuth.getHeader("x-csrf-token"),
                                responseGetAuth.getCookie("auth_sid")
                        );


        //GET user which tried to delete
        Response testDeleteSecondUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + secondUserId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));


        //GET firstuser
        Response testDeleteFirstUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + firstUserId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

         ////ожидала увидеть ошибку 400? и текст сообщения о невозможности удаления пользователя
        // будучи авторизованным под другим пользователем
        //в итоге запрос выполнился успешно 200 и пользватель 1 (под кем была авторизация) удаляется
        //Пользователь2 не удалился+

        assertAll("Сложный сценарий проверки операции удаления",
                () -> Assertions.assertResponseCodeEquals(responseDeleteUser, 400),
                () -> Assertions.assertResponseCodeEquals(testDeleteSecondUserData, 200),
                () -> Assertions.assertResponseCodeEquals(testDeleteFirstUserData, 200)
        );


    }
}

