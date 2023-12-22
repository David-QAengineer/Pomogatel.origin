import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LoginApiPhone {
    private static final String BASE_URL="https://api3.pomogatel.ru/";
    public String access_token;




    @Test(priority = 0)
    public void LoginWithPhone() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("login.properties"));
        String password_user=System.getProperty("password");
        String phone_user=System.getProperty("phone");
        JSONObject params_login = new JSONObject();
        params_login.put("password", password_user);
        params_login.put("phone", phone_user);
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
    @Test(priority = 1)
    public void SettingsValues() {
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response = given().
                when().
                get("settings/values");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema.json"));
        System.out.println("pretty " + response.body().asPrettyString());
    }

    @Test(priority = 2)
    public void Settings(){
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response=given().
                when().
                get("settings");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema1.json"));
        System.out.println(response.body().asPrettyString());

    }
@Test(priority = 3)
    public void State(){
        Response response=given().
                when().
                header("Authorization", "Bearer " + access_token).
                contentType("application/json").accept("application/json").
                get("https://api19.pomogatel.ru/billing/state").then().extract().response();
    System.out.println("fredi " + response.body().asPrettyString());
    response.then().assertThat().body(JsonSchemaValidator.
            matchesJsonSchemaInClasspath("schema2.json"));
}
    @Test(priority = 4)
    public void Interest(){
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response=given().when().header("Authorization", "Bearer " + access_token).get("accounts/user/interest");
        System.out.println("response  " + response.body().asPrettyString());
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema3.json"));
    }
}

