package org.example;

import flow.Flows;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProfileUser extends BaseApi {
    private static final String BASE_URL="https://api3.pomogatel.ru/";

 @Test
    public void EditName(){
     Flows flows=new Flows();
        String access_token=flows.getAccessToken();
        String customer_id=flows.getStates(access_token).body().jsonPath().getString("customerAccountState.customerPerson.id");
        SpecificationApi.InstallSpecification(SpecificationApi.requestSpecification(BASE_URL),
                SpecificationApi.responseSpecification200());
        JSONObject userProfile=new JSONObject();
        userProfile.put("firstName","МАРИНА");
        userProfile.put("genderId", 1);
        userProfile.put("id", customer_id);
        userProfile.put("lastName", "МАРИАННА");
        userProfile.put("middleName","Маринаа");
        Response response=given().when().body(userProfile.toString()).
                header("Authorization", "Bearer " + access_token).put("accounts/customer/person");
        System.out.println(("123   "  + response.body().asPrettyString()));
    }
}
