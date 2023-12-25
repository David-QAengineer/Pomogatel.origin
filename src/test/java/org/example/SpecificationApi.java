package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecificationApi {
    public static RequestSpecification requestSpecification(String url){
        return new RequestSpecBuilder().setBaseUri(url).
                setContentType(ContentType.JSON).
                setAccept("application/json").
                build();

    }
    public static ResponseSpecification responseSpecification200(){
        return new ResponseSpecBuilder().
                 expectStatusCode(200).
                build();
    }

    public static void InstallSpecification(RequestSpecification request, ResponseSpecification response){
        RestAssured.requestSpecification=request;
        RestAssured.responseSpecification=response;
    }

}
