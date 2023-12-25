package flow;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import java.io.IOException;
import static io.restassured.RestAssured.given;


public class Flows {
    private static final String BaseUrl="https://api3.pomogatel.ru/";
    public String getAccessToken()   {
        try {
            System.getProperties().load(ClassLoader.getSystemResourceAsStream("login.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String password_user=System.getProperty("password");
        String phone_user=System.getProperty("phone");
        JSONObject params_login = new JSONObject();
        params_login.put("password", password_user);
        params_login.put("phone", phone_user);
        Response response = given().when().body(params_login.toString()).contentType(ContentType.JSON).
                post("https://api3.pomogatel.ru/users/login/phone")
                .then().extract().response();
        return response.body().jsonPath().getString("accessToken");

    }
    public Response getStates(String access_token){
        return given().when().header("Authorization","Bearer " + access_token).get(BaseUrl+"states");
    }
}
