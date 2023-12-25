package org.example;

import flow.Flows;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginApiPhone extends BaseApi{

    private static final String BASE_URL="https://api3.pomogatel.ru/";




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
        this.access_token=access_token;
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
        System.out.println("settings_values " + response.body().asPrettyString());
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
        System.out.println("settings " + response.body().asPrettyString());

    }
@Test(priority = 3)
    public void State(){
   // Flows flows=new Flows();
   // String access_token=flows.getAccessToken();
        Response response=given().
                when().
                header("Authorization", "Bearer " + access_token).
                contentType(ContentType.JSON).accept("application/json").
                get("https://api19.pomogatel.ru/billing/state");
        response.then().statusCode(200).extract().response();
    System.out.println("State " + response.body().asPrettyString());
    response.then().assertThat().body(JsonSchemaValidator.
            matchesJsonSchemaInClasspath("schema2.json"));

}
    @Test(priority = 4)
    public void Interest(){
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response=given().when().header("Authorization", "Bearer " + access_token).get("accounts/user/interest");
        System.out.println("interest  " + response.body().asPrettyString());
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema3.json"));
        String id=response.body().jsonPath().getString("id").toString();
        this.id=id;
        assertThat(id,hasToString(id));
        assertThat(response.body().jsonPath().getString("country"), equalTo("Россия"));
        System.out.println("id " + id.toString());
    }
    @Test(priority = 5)
    public void User(){
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response=given().
                when().
                header("Authorization", "Bearer " + access_token).
                get("accounts/user");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema5.json"));
        String user_id=response.body().jsonPath().getString("userId");
        this.user_id=user_id;
        System.out.println("user_id " + user_id);

    }

    @Test(priority = 6)
    public void States(){
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        Response response=given().when().header("Authorization","Bearer " + access_token).get("states");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema6.json"));
        System.out.println("states " + response.body().asPrettyString());
        String customer_id=response.body().jsonPath().getString("customerAccountState.customerPerson.id");
        String address_id=response.body().jsonPath().getString("customerAccountState.customerAddress.id");
        this.address_id=address_id;
        this.customer_id=customer_id;
        System.out.println("customer_id " + customer_id.toString());
        System.out.println("address_id " + address_id.toString());

    }

    @Test(priority = 7)
    public void Negotiate(){
        Response response=given().
                when().
                contentType("application/json").
                accept("application/json").
                header("Authorization", "Bearer "+ access_token).
                post("https://hubs.pomogatel.ru/notifications/negotiate");
        response.then().assertThat().body(JsonSchemaValidator.
                matchesJsonSchemaInClasspath("schema4.json"));
        System.out.println("negotiate " + response.body().asPrettyString());
    }

}

