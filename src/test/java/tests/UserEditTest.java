package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import java.util.HashMap;
import java.util.Map;


public class UserEditTest extends BaseTestCase {
    private  final ApiCoreRequests apiCoreRequests=new ApiCoreRequests ();

    @Test
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
}
