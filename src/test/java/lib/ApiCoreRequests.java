package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.http.Header;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a get request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a get request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a get request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a get request")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a post request")
    public Response makePostRequest(String url, Map<String, String> bodyData) {
        return given()
                .filter(new AllureRestAssured())
                .body(bodyData)
                .post(url)
                .andReturn();
            }

    @Step("Make a post request with jsonpath")
    public JsonPath makePostRequestAndJsonPath(String url, Map<String, String> bodyData) {
        return given()
                .filter(new AllureRestAssured())
                .body(bodyData)
                .post(url)
                .jsonPath();
    }

    @Step("Make a put request")
    public Response makePutRequest(String url, String token, String cookie, Map<String, String> bodyData) {
        return  given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .body(bodyData)
                .put(url)
                .andReturn();
    }

    @Step("Make a put request without token and cookie")
    public Response makePutRequestNotAuth(String url, Map<String, String> bodyData) {
        return  given()
                .filter(new AllureRestAssured())
                .body(bodyData)
                .put(url)
                .andReturn();
    }

    @Step("Make a delete request")
    public Response makeDeleteRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }
}
