package clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;


public class ApiClient {
    protected RequestSpecification givenWithHeaders() {
        return given()
                .contentType("application/json")
                .accept("application/json");
    }


    protected Response post(String path, Object body) {
        return givenWithHeaders().body(body).when().post(path).then().extract().response();
    }


    protected Response get(String path) {
        return givenWithHeaders().when().get(path).then().extract().response();
    }


    protected Response put(String path, Object body) {
        return givenWithHeaders().body(body).when().put(path).then().extract().response();
    }


    protected Response delete(String path) {
        return givenWithHeaders().when().delete(path).then().extract().response();
    }
}