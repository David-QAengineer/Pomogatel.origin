import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LoginApiPhone {
    private static final String BASE_URL="https://api3.pomogatel.ru/";
    public String access_token;




    @Test
    public void LoginWithPhone() {
        JSONObject params_login = new JSONObject();
        params_login.put("password", "622521");
        params_login.put("phone", "9021928531");
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response = given().when().body(params_login.toString()).
                post("users/login/phone")
                .then().extract().response();
        String access_token = response.body().jsonPath().getString("accessToken");
        this.access_token = access_token;
        System.out.println(access_token);
        //---------

    }
    @Test
    public void SettingsValues() {
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response = given().
                body(access_token).
                when().
                get("settings/values");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema.json"));
        System.out.println("pretty " + response.body().asPrettyString());
    }

    @Test
    public void Settings(){
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response=given().
                body(access_token).
                when().
                get("settings");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema.json1"));
        System.out.println(response.body().asPrettyString());

    }

}

