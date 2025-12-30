package clients;

import io.restassured.response.Response;
import models.AuthResponse;


import java.util.Map;


public class AuthClient extends ApiClient {
    public AuthResponse login(Map<String, Object> payload) {
        Response res = post("/auth/login", payload);
        System.out.println("Login api response: "+res.asPrettyString());
        return res.as(AuthResponse.class);
    }
}